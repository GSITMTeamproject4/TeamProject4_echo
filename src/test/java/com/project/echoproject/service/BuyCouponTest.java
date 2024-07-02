package com.project.echoproject.service;

import com.project.echoproject.entity.*;
import com.project.echoproject.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class BuyCouponTest {
    @Autowired
    private ChallengeService challengeService;

    private SiteUser user;


    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SiteUserService siteUserService;

    @Autowired
    private PointService pointService;

    @Autowired
    private UserCouponService userCouponService;


    private Point point;

    @BeforeEach
    void setUp() {
        // 테스트용 SiteUser 생성 및 저장
        user = new SiteUser();
        Image image = new Image();
        image.setFilePath("/uploads/profile.jpg");
        imageRepository.save(image);
        user.setUserId("testUser");
        user.setUserName("Test User");
        user.setPassword(passwordEncoder.encode("password")); // 비밀번호 암호화
        user.setEmail("test@example.com");
        user.setProfileImage(image);
        user.setCurrentPoint(12000L);
        siteUserRepository.save(user);
    }

    @BeforeEach
    void RegisterCouponByAdmin() throws IOException {
        String name = "스타벅스";
        Long point = 5000L;
        // 실제 이미지 파일 로드
        ClassPathResource resource = new ClassPathResource("static/img/스타벅스.PNG");
        InputStream inputStream = resource.getInputStream();
        MultipartFile file = new MockMultipartFile("file", "스타벅스.PNG", "image/PNG", inputStream);

        couponService.addCoupon(name,point,file);
    }
    @Commit
    @Test
    void buyCoupon() throws IOException {

        // Given
        Long couponId = 1L;

        // When
        Coupon coupon = couponService.getCoupon(couponId);

        Long balance = user.getCurrentPoint() - coupon.getCouponPoint();

        // 현재포인트 update
        user = siteUserService.buyCoupon(user.getUserId(), balance);

        Point addPoint = new Point();
        addPoint.setSiteUser(user);
        addPoint.setPointInfo("coupon");
        addPoint.setPoint(coupon.getCouponPoint());
        addPoint.setInsertDate(LocalDateTime.now());

        pointService.addPointHistory(addPoint);

        UserCoupon addCoupon = new UserCoupon();
        addCoupon.setUserId(user);
        addCoupon.setCoupon(coupon);
        addCoupon.setInsertDate(LocalDateTime.now());

        userCouponService.addCoupon(addCoupon);

        // Then
        assertNotNull(user);
        assertEquals(balance, user.getCurrentPoint());

    }
}
