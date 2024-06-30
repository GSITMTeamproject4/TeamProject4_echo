package com.project.echoproject.service;

import com.project.echoproject.dto.SiteUserEditForm;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class MypageServiceImpl implements MypageService {

    private final SiteUserRepository userRepository;
    private final ImageService imageService;

    @Autowired
    public MypageServiceImpl(SiteUserRepository userRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public SiteUser getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    @Override
    public void updateUser(String userId, SiteUserEditForm updatedUser, MultipartFile file) throws IOException {
        SiteUser user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUserName(updatedUser.getUserName());
        user.setNickName(updatedUser.getNickName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNum(updatedUser.getPhoneNum());
        user.setGender(updatedUser.getGender());
        user.setZipcode(updatedUser.getZipcode());
        user.setStreetaddr(updatedUser.getStreetaddr());
        user.setDetailaddr(updatedUser.getDetailaddr());

        if (file != null && !file.isEmpty()) {
            Image image = imageService.saveImage(file);
            user.setProfileImage(image);
        }


        userRepository.save(user);
    }


    @Transactional
    @Override
    public void deleteUser(String userId) {
        SiteUser user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        userRepository.delete(user);
    }


    @Override
    public String encodeImageToBase64(String filePath) throws IOException {
        return imageService.encodeImageToBase64(filePath);
    }
}
