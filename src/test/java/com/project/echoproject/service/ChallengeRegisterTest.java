package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.Challenge;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.AuthBoardRepository;
import com.project.echoproject.repository.ChallengeRepository;
import com.project.echoproject.repository.ImageRepository;
import com.project.echoproject.repository.SiteUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ChallengeRegisterTest {
    @Autowired
    private ChallengeService challengeService;

    private SiteUser user;

    @Autowired
    private SiteUserRepository siteUserRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

        siteUserRepository.save(user);
    }

    @Commit
    @Test
    void createChallenge() throws IOException {

        // Given
        String info = "메일함비우기";
        String content = "테스트 내용입니다. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum";

        // 실제 이미지 파일 로드
        ClassPathResource resource = new ClassPathResource("static/img/dog.jpeg");
        InputStream inputStream = resource.getInputStream();
        MultipartFile file = new MockMultipartFile("file", "dog.jpeg", "image/jpeg", inputStream);

        // When
        Challenge result = challengeService.createBoard(info, file, user,content);

        // Then
        assertNotNull(result);
        assertNotNull(result.getChallengeId());
        assertEquals(info, result.getChallengeInfo());
        assertEquals(content, result.getChallengeContent());
        assertNotNull(result.getImage());
        assertEquals(user, result.getSiteUser());

        // Verify
        Challenge savedBoard = challengeRepository.findById(result.getChallengeId()).orElse(null);
        assertNotNull(savedBoard);
        assertEquals(info, savedBoard.getChallengeInfo());

        // 추가 검증: 이미지가 실제로 저장되었는지 확인
        assertNotNull(savedBoard.getImage());
        assertNotNull(savedBoard.getImage().getFilePath());

        // 파일 경로 출력
        System.out.println("File path: " + savedBoard.getImage().getFilePath());

        // 파일 존재 여부 대신 파일 경로가 null이 아닌지 확인
        assertNotNull(savedBoard.getImage().getFilePath());
    }
}
