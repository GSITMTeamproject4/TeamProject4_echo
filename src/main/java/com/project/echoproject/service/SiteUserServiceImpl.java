package com.project.echoproject.service;

import com.project.echoproject.email.EmailService;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserRole;
import com.project.echoproject.repository.SiteUserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SiteUserServiceImpl implements SiteUserService {

    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(SiteUserServiceImpl.class);


    @Override
    public SiteUser findByUserId(String userId) {
        return siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));
    }

    @Override
    public SiteUser create(String userId, String userName, String phoneNum, String gender, String password, String email, MultipartFile file, String address) throws IOException {
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

        if ("male".equalsIgnoreCase(gender)) {
            siteUser.setGender("M");
        } else if ("female".equalsIgnoreCase(gender)) {
            siteUser.setGender("F");
        }

        siteUser.setPassword(passwordEncoder.encode(password));

        if (file != null && !file.isEmpty()) {
            Image image = imageService.saveImage(file);
            siteUser.setProfileImage(image);
            image.setSiteUser(siteUser);
        }

        siteUser.setProvider("local");
        siteUser.setProviderId(userId);
        siteUser.setRole(UserRole.USER);

        return siteUserRepository.save(siteUser);
    }

    @Override
    public SiteUser buyCoupon(String userId, Long balance) {
        SiteUser siteUser = findByUserId(userId);
        siteUser.setCurrentPoint(balance);
        return siteUserRepository.save(siteUser);
    }

    @Override
    public void requestPasswordReset(String email) {
        SiteUser user = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
        siteUserRepository.save(user);

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        } catch (MessagingException e) {
            // 로깅 추가
            logger.error("Failed to send password reset email", e);
            // 사용자 정의 예외를 던지거나 다른 방식으로 오류 처리
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        SiteUser user = siteUserRepository.findByResetToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with reset token: " + token));

        if (user.getResetTokenExpiry().isAfter(LocalDateTime.now())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null);
            user.setResetTokenExpiry(null);
            siteUserRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyUserAndSendResetEmail(String userId, String email, String userName) {
        logger.info("Attempting to find user with userId: {}, email: {}, userName: {}", userId, email, userName);
        Optional<SiteUser> siteUser = siteUserRepository.findByUserIdAndEmailAndUserName(userId, email, userName);
        if (siteUser != null) {
            logger.info("User found. Sending reset email.");
            // 이메일 발송 로직
            requestPasswordReset(email);
            return true;
        }
        logger.warn("User not found with provided information");
        return false;
    }

    @Override
    public void resendPasswordResetEmail(String email) {
        SiteUser user = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        requestPasswordReset(email);
    }

    @Override
    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }
}