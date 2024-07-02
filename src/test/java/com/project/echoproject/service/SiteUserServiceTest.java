package com.project.echoproject.service;




import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.ImageRepository;
import com.project.echoproject.repository.SiteUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SiteUserServiceTest {


    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ImageRepository imageRepository;

    @Test
    @Commit
    public void testSiteUserService() {
        // 테스트용 SiteUser 생성 및 저장
        SiteUser user = new SiteUser();
        Image image = new Image();
        image.setFilePath("/uploads/profile.jpg");
        imageRepository.save(image);
        user.setUserId("testUser");
        user.setUserName("Test User");
        user.setPassword(passwordEncoder.encode("password")); // 비밀번호 암호화
        user.setEmail("test@example.com");
        user.setProfileImage(image);



        siteUserRepository.save(user);
    }
}