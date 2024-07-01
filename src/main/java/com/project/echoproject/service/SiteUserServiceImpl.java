package com.project.echoproject.service;

import com.project.echoproject.email.EmailService;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.Point;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UserRole;
import com.project.echoproject.repository.SiteUserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
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
            logger.error("비밀번호 재설정 이메일 전송 실패", e);
            // 예외 처리 방법에 따라 RuntimeException 또는 다른 예외 던지기
            throw new RuntimeException("비밀번호 재설정 이메일 전송 실패", e);
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
        Optional<SiteUser> siteUser = siteUserRepository.findByUserIdAndEmailAndUserName(userId, email, userName);
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
        SiteUser user = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
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
}
