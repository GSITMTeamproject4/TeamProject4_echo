package com.project.echoproject.service;

import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.Point;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.ImageRepository;
import com.project.echoproject.repository.SiteUserRepository;
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

@SpringBootTest
@Transactional
public class AdminTest {

    private SiteUser user;


    @Autowired
    private SiteUserRepository siteUserRepository;


    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        // 테스트용 관리자 생성 및 저장
        user = new SiteUser();
        Image image = new Image();
        image.setFilePath("/uploads/profile.jpg");
        imageRepository.save(image);
        user.setUserId("admin1234");
        user.setUserName("ADMIN");
        user.setPassword(passwordEncoder.encode("admin1234")); // 비밀번호 암호화
        user.setEmail("admin1234@example.com");
        user.setProfileImage(image);
        user.setCurrentPoint(12000L);
        siteUserRepository.save(user);
    }

    @Test
    @Commit
    void addProduct() throws IOException {
        String name = "에코백";
        int price = 7000;
        // 실제 이미지 파일 로드
        ClassPathResource resource = new ClassPathResource("static/img/에코백.png");
        InputStream inputStream = resource.getInputStream();
        MultipartFile file = new MockMultipartFile("file", "에코백.png", "image/png", inputStream);

        productService.addItem(name,price,file);
    }
}
