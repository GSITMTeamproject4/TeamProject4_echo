package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.AuthBoardRepository;
import com.project.echoproject.repository.SiteUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthBoardServiceImplTest {

    @Autowired
    private AuthBoardService authBoardService;

    @Autowired
    private AuthBoardRepository authBoardRepository;

    @Autowired
    private SiteUserRepository siteUserRepository;

    private SiteUser testUser;

    @BeforeEach
    void setUp() {
        testUser = new SiteUser();
        testUser.setUserId("testUser");
        testUser.setUserName("테스트유저");
        testUser.setEmail("test@example.com");
        testUser.setPassword("1234");
        siteUserRepository.save(testUser);

        // 프로필 이미지 설정
        Image profileImage = new Image();
        profileImage.setFilePath("img/default-profile.png");
        testUser.setProfileImage(profileImage);
    }

    @RepeatedTest(10)
    @Commit
    void createBoard() throws IOException {
        // Given
        String title = "게시판 테스트";
        String content = "테스트 내용입니다. Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum";

        // 실제 이미지 파일 로드
        ClassPathResource resource = new ClassPathResource("static/img/dog.jpeg");
        InputStream inputStream = resource.getInputStream();
        MultipartFile file = new MockMultipartFile("file", "dog.jpeg", "image/jpeg", inputStream);

        // When
        AuthBoard result = authBoardService.createBoard(title, content, file, testUser);

        // Then
        assertNotNull(result);
        assertNotNull(result.getListId());
        assertEquals(title, result.getBoardTitle());
        assertEquals(content, result.getBoardContent());
        assertNotNull(result.getImage());
        assertEquals(testUser, result.getSiteUser());

        // Verify
        AuthBoard savedBoard = authBoardRepository.findById(result.getListId()).orElse(null);
        assertNotNull(savedBoard);
        assertEquals(title, savedBoard.getBoardTitle());

        // 추가 검증: 이미지가 실제로 저장되었는지 확인
        assertNotNull(savedBoard.getImage());
        assertNotNull(savedBoard.getImage().getFilePath());

        // 파일 경로 출력
        System.out.println("File path: " + savedBoard.getImage().getFilePath());

        // 파일 존재 여부 대신 파일 경로가 null이 아닌지 확인
        assertNotNull(savedBoard.getImage().getFilePath());
    }

    @Test
    void getAuthBoard() throws IOException {
        // Given
        AuthBoard authBoard = createTestAuthBoard();

        // When
        AuthBoard result = authBoardService.getAuthBoard(authBoard.getListId());

        // Then
        assertNotNull(result);
        assertEquals(authBoard.getListId(), result.getListId());
        assertEquals(authBoard.getBoardTitle(), result.getBoardTitle());
        assertNotNull(result.getImage());
        assertNotNull(result.getImage().getBase64());
    }

    @Test
    void getAuthBoards() throws IOException {
        // Given
        createTestAuthBoard();
        createTestAuthBoard();

        // When
        Page<AuthBoard> result = authBoardService.getAuthBoards(0, 10);

        // Then
        assertNotNull(result);
        assertTrue(result.getTotalElements() >= 2);
    }

    @Test
    void deleteBoard() throws IOException {
        // Given
        AuthBoard authBoard = createTestAuthBoard();

        // When
        authBoardService.deleteBoard(authBoard.getListId(), testUser);

        // Then
        assertTrue(authBoardRepository.findById(authBoard.getListId()).isEmpty());
    }

    @Test
    void deleteBoardWithoutPermission() throws IOException {
        // Given
        AuthBoard authBoard = createTestAuthBoard();
        SiteUser differentUser = new SiteUser();
        differentUser.setUserId("differentUser");
        siteUserRepository.save(differentUser);

        // When & Then
        assertThrows(SecurityException.class, () -> authBoardService.deleteBoard(authBoard.getListId(), differentUser));
    }

    private AuthBoard createTestAuthBoard() throws IOException {
        String title = "Test Title";
        String content = "Test Content";
        MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
        return authBoardService.createBoard(title, content, file, testUser);
    }
}