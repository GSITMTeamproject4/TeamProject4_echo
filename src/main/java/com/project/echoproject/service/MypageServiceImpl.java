package com.project.echoproject.service;

import com.project.echoproject.dto.SiteUserEditForm;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service  // 이 클래스가 서비스 계층의 빈으로 등록되도록 표시합니다.
public class MypageServiceImpl implements MypageService {

    private final SiteUserRepository userRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Autowired  // 생성자를 통해 의존성을 주입합니다.
    public MypageServiceImpl(SiteUserRepository userRepository, ImageService imageService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.imageService = imageService;
        this.passwordEncoder = passwordEncoder;
    }

    // 주어진 사용자 ID로 사용자를 검색합니다.
    @Override
    public SiteUser getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    // 주어진 사용자 ID에 해당하는 사용자의 정보를 업데이트합니다.
    @Override
    public void updateUser(String userId, SiteUser updatedUser, MultipartFile file) throws IOException {
        SiteUser updateInfo = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        updateInfo.setUserName(updatedUser.getUserName());
        updateInfo.setNickName(updatedUser.getNickName());
        updateInfo.setEmail(updatedUser.getEmail());
        updateInfo.setPhoneNum(updatedUser.getPhoneNum());
        updateInfo.setZipcode(updatedUser.getZipcode());
        updateInfo.setStreetaddr(updatedUser.getStreetaddr());
        updateInfo.setDetailaddr(updatedUser.getDetailaddr());

        if (file != null && !file.isEmpty()) {
            Image newImage = imageService.saveImage(file);
            if (updateInfo.getProfileImage() != null && !updateInfo.getProfileImage().isDefaultImage()) {
                imageService.deleteImage(updateInfo.getProfileImage());
            }
            updateInfo.setProfileImage(newImage);
        }

        userRepository.save(updateInfo);
    }

    @Override
    public void updateUser(String userId, SiteUserEditForm updatedUser) throws IOException {
        SiteUser user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUserName(updatedUser.getUserName());
        user.setNickName(updatedUser.getNickName());
        user.setEmail(updatedUser.getEmail());
        user.setPhoneNum(updatedUser.getPhoneNum());
        user.setZipcode(updatedUser.getZipcode());
        user.setStreetaddr(updatedUser.getStreetaddr());
        user.setDetailaddr(updatedUser.getDetailaddr());

        if (updatedUser.getProfileImage() != null && !updatedUser.getProfileImage().isEmpty()) {
            Image newImage = imageService.saveImage(updatedUser.getProfileImage());
            if (user.getProfileImage() != null && !user.getProfileImage().isDefaultImage()) {
                imageService.deleteImage(user.getProfileImage());
            }
            user.setProfileImage(newImage);
        }

        userRepository.save(user);
    }

    // 주어진 사용자 ID에 해당하는 사용자를 삭제합니다.
    @Transactional  // 트랜잭션 범위 내에서 실행되도록 표시합니다.
    @Override
    public void deleteUser(String userId) {
        SiteUser user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        userRepository.delete(user);  // 사용자 정보를 삭제합니다.
    }

    // 주어진 파일 경로에 해당하는 이미지를 Base64 형식으로 인코딩합니다.
    @Override
    public String encodeImageToBase64(String filePath) throws IOException {
        return imageService.encodeImageToBase64(filePath);
    }
}
