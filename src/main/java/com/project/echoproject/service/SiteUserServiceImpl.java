package com.project.echoproject.service;

import com.project.echoproject.email.EmailService;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.Point;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserRole;
import com.project.echoproject.repository.SiteUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SiteUserServiceImpl implements SiteUserService {

    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ImageService imageService;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(SiteUserServiceImpl.class);
    private final PointService pointService;

    // 사용자 ID로 사용자 정보 조회
    @Override
    public SiteUser findByUserId(String userId) {
        return siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));
    }

    // 사용자 생성 메서드
    @Override
    public SiteUser create(String userId, String userName, String phoneNum, String gender, String password, String email, MultipartFile file, String zipcode, String streetaddr, String detailaddr) throws IOException {
        Optional<SiteUser> existingUser = siteUserRepository.findByUserId(userId);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(userId);
        siteUser.setUserName(userName);
        siteUser.setEmail(email);
        siteUser.setPhoneNum(phoneNum);
        siteUser.setZipcode(zipcode);
        siteUser.setStreetaddr(streetaddr);
        siteUser.setDetailaddr(detailaddr);

        if (userName == null || userName.isEmpty()) {
            siteUser.setUserName(userId);  // userName이 없으면 userId를 사용
        } else {
            siteUser.setUserName(userName);
        }

        if (gender != null) {
            if ("male".equalsIgnoreCase(gender)) {
                siteUser.setGender("M");
            } else if ("female".equalsIgnoreCase(gender)) {
                siteUser.setGender("F");
            }
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

    // 쿠폰 구매 메서드
    @Override
    public SiteUser buyCoupon(String userId, Long balance) {
        SiteUser siteUser = findByUserId(userId);
        siteUser.setCurrentPoint(balance);
        return siteUserRepository.save(siteUser);
    }

    // 모든 사용자 조회 메서드
    public List<SiteUser> getAllUsers() {
        return siteUserRepository.findAll();
    }

    // 비밀번호 초기화 요청 메서드
    @Override
    public void requestPasswordReset(String email) {
        List<SiteUser> users = siteUserRepository.findByEmail(email);
        if (users.isEmpty()) {
            logger.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        boolean hasLocalAccount = false;
        List<String> socialProviders = new ArrayList<>();
        SiteUser localUser = null;

        for (SiteUser user : users) {
            if ("local".equals(user.getProvider())) {
                hasLocalAccount = true;
                localUser = user;
            } else {
                socialProviders.add(user.getProvider());
            }
        }

        if (hasLocalAccount) {
            String token = UUID.randomUUID().toString();
            localUser.setResetToken(token);
            localUser.setResetTokenExpiry(LocalDateTime.now().plusHours(24));
            siteUserRepository.save(localUser);

            try {
                emailService.sendPasswordResetEmail(email, token, socialProviders);
            } catch (MessagingException e) {
                logger.error("Failed to send password reset email to: {}", email, e);
                throw new RuntimeException("Failed to send password reset email", e);
            }
        } else if (!socialProviders.isEmpty()) {
            try {
                emailService.sendSocialLoginReminderEmail(email, socialProviders);
            } catch (MessagingException e) {
                logger.error("Failed to send social login reminder email to: {}", email, e);
                throw new RuntimeException("Failed to send social login reminder email", e);
            }
        }
    }

    // 비밀번호 재설정 메서드
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

    // 사용자 확인 후 비밀번호 재설정 이메일 전송 메서드
    @Override
    public boolean verifyUserAndSendResetEmail(String userId, String email, String userName) {
        logger.info("userId: {}, email: {}, userName: {}로 사용자 확인 시도", userId, email, userName);
        List<SiteUser> users = siteUserRepository.findByEmail(email);
        Optional<SiteUser> siteUser = users.stream()
                .filter(user -> user.getUserId().equals(userId) && user.getUserName().equals(userName))
                .findFirst();
        if (siteUser.isPresent()) {
            logger.info("사용자 확인. 비밀번호 재설정 이메일 전송 시도");
            requestPasswordReset(email);
            return true;
        }
        logger.warn("제공된 정보로 사용자를 찾을 수 없음");
        return false;
    }

    // 비밀번호 재설정 이메일 재전송 메서드
    @Override
    public void resendPasswordResetEmail(String email) {
        List<SiteUser> users = siteUserRepository.findByEmail(email);
        if (users.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        requestPasswordReset(email);
    }

    // 비밀번호 재설정 토큰 생성 메서드
    @Override
    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    // 관리자가 사용자에게 포인트 추가하는 메서드
    public void addPointByAdmin(String userId, Long point, String challengeInfo) {
        SiteUser siteUser = findByUserId(userId);
        siteUser.setCurrentPoint(siteUser.getCurrentPoint() + point); // 현재 포인트에 point를 추가
        siteUserRepository.save(siteUser);

        LocalDateTime now = LocalDateTime.now();
        Point addPoint = new Point();
        addPoint.setSiteUser(siteUser);
        addPoint.setPoint(500L); // 임의의 포인트 값 설정 (참고용으로 남겼습니다)
        addPoint.setPointInfo(challengeInfo);
        addPoint.setInsertDate(now);
        pointService.addPointHistory(addPoint); // 포인트 히스토리 추가
    }

    // 이메일로 아이디 찾기
    @Override
    public String findUserIdByEmail(String email) {
        logger.info("Attempting to find user ID for email: {}", email);
        List<SiteUser> users = siteUserRepository.findByEmail(email);
        if (users.isEmpty()) {
            logger.warn("No user found for email: {}", email);
            throw new RuntimeException("해당 이메일로 등록된 사용자가 없습니다.");
        }
        // 여러 사용자가 있을 경우 첫 번째 사용자의 ID를 반환
        return users.get(0).getUserId();
    }

    // 이메일 아이디 찾기 전송
    @Override
    public void sendUserIdEmail(String email) throws MessagingException {
        logger.info("Attempting to send user ID email to: {}", email);
        List<SiteUser> users = siteUserRepository.findByEmail(email);
        if (users.isEmpty()) {
            logger.warn("No user found for email: {}", email);
            throw new RuntimeException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        try {
            emailService.sendUserIdEmail(email);
            logger.info("User ID email sent successfully to: {}", email);
        } catch (MessagingException e) {
            logger.error("Failed to send user ID email to: {}", email, e);
            throw e;
        }
    }

    @Override
    public List<String> checkEmailDuplication(String email) {
        List<SiteUser> users = siteUserRepository.findByEmail(email);
        if (users.isEmpty()) {
            return Collections.emptyList();
        }
        return users.stream()
                .map(user -> user.getProvider())
                .collect(Collectors.toList());
    }


}
