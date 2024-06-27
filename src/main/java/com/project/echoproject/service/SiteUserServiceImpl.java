package com.project.echoproject.service;

import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SiteUserServiceImpl implements SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;

    @Override
    public SiteUser findByUserId(String userId) {
        return siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));
    }

    @Override
    public SiteUser create(String userId, String userName, String phoneNum, String gender, String password, String email, MultipartFile file,String address) throws IOException {
        Optional<SiteUser> existingUser = siteUserRepository.findByUserId(userId);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(userId);
        siteUser.setUserName(userName);
        siteUser.setEmail(email);
        siteUser.setPhoneNum(phoneNum);
        siteUser.setAddress(address);
        // Gender 값을 줄임
        if ("male".equalsIgnoreCase(gender)) {
            siteUser.setGender("M");
        } else if ("female".equalsIgnoreCase(gender)) {
            siteUser.setGender("F");
        }

        siteUser.setPassword(passwordEncoder.encode(password));

        // 이미지 파일 처리
        if (file != null && !file.isEmpty()) {
            Image image = imageService.saveImage(file);
            siteUser.setImgUrl(image.getFilePath());
        }

        return siteUserRepository.save(siteUser);
    }

    public SiteUser buyCoupon(String userId, Long balance) {
        SiteUser siteUser = findByUserId(userId);
        siteUser.setCurrentPoint(balance);
        return siteUserRepository.save(siteUser);
    }

    public List<SiteUser> getAllUsers(){
        return siteUserRepository.findAll();
    }
}