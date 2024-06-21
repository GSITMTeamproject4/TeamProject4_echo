package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.AuthBoardRepository;
import com.project.echoproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthBoardServiceImpl implements AuthBoardService {

    private final AuthBoardRepository authBoardRepository;
    private final ImageRepository imageRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthBoardServiceImpl.class);

    private static final String UPLOAD_DIR = "uploads/";

    @Override
    public AuthBoard saveBoard(String title, String content, MultipartFile file, SiteUser siteUser) throws IOException {
        AuthBoard authBoard = new AuthBoard();
        authBoard.setBoardTitle(title);
        authBoard.setBoardContent(content);
        authBoard.setPostCreateDate(LocalDateTime.now());

        if (!file.isEmpty()) {
            // 프로젝트 루트 디렉토리의 절대 경로를 가져옴
            String rootPath = new File("").getAbsolutePath();
            String uploadPath = rootPath + File.separator + UPLOAD_DIR;

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs(); // uploads 폴더가 존재하지 않으면 생성
            }

            logger.info("Upload directory: " + uploadPath);

            // 파일 저장 경로 생성
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            File dest = new File(uploadDir, fileName);
            file.transferTo(dest);

            logger.info("File saved to: " + dest.getAbsolutePath());

            // 이미지 엔티티 생성 및 저장
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setFilePath(fileName); // 파일 이름만 저장
            image.setUploadTime(LocalDateTime.now());
            authBoard.setImage(image);
            imageRepository.save(image); // 이미지 저장
            authBoard.setCheckImg(fileName); // checkImg 필드 설정
            authBoard.setSiteUser(siteUser); // siteUser 설정
        } else {
            throw new IOException("File is empty");
        }

        return authBoardRepository.save(authBoard);
    }

    @Override
    public String encodeImageToBase64(String filePath) throws IOException {
        String rootPath = new File("").getAbsolutePath();
        String fullPath = rootPath + File.separator + UPLOAD_DIR + filePath;
        byte[] imageData = java.nio.file.Files.readAllBytes(new File(fullPath).toPath());
        return Base64.getEncoder().encodeToString(imageData);
    }

    @Override
    public AuthBoard getAuthBoard(Long id) {
        AuthBoard authBoard = authBoardRepository.findById(id).orElse(null);
        if (authBoard != null && authBoard.getImage() != null) {
            try {
                String base64Image = encodeImageToBase64(authBoard.getImage().getFilePath());
                authBoard.getImage().setBase64(base64Image); // Base64 문자열 설정
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return authBoard;
    }

    @Override
    public List<AuthBoard> getAllBoards() {
        return authBoardRepository.findAll();
    }

    @Override
    public AuthBoard getBoardById(Long id) {
        return authBoardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid board Id:" + id));
    }
}
