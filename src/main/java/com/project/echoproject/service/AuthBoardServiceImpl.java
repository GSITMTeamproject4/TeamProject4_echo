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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthBoardServiceImpl implements AuthBoardService {

    private final AuthBoardRepository authBoardRepository;
    private final ImageService imageService; // ImageService 주입
    private final ImageRepository imageRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthBoardServiceImpl.class);

    @Override
    public AuthBoard saveBoard(String title, String content, MultipartFile file, SiteUser siteUser) throws IOException {
        AuthBoard authBoard = new AuthBoard();
        authBoard.setBoardTitle(title);
        authBoard.setBoardContent(content);
        authBoard.setPostCreateDate(LocalDateTime.now());

        if (!file.isEmpty()) {
            // ImageService를 사용하여 이미지 저장
            Image image = imageService.saveImage(file);
            authBoard.setImage(image);
            authBoard.setCheckImg(image.getFilePath()); // checkImg 필드 설정
            authBoard.setSiteUser(siteUser); // siteUser 설정
        } else {
            throw new IOException("File is empty");
        }

        return authBoardRepository.save(authBoard);
    }

    @Override
    public String encodeImageToBase64(String filePath) throws IOException {
        return imageService.encodeImageToBase64(filePath);
    }

    @Override
    public AuthBoard getAuthBoard(Long id) {
        AuthBoard authBoard = authBoardRepository.findById(id).orElse(null);
        if (authBoard != null && authBoard.getImage() != null) {
            try {
                String base64Image = imageService.encodeImageToBase64(authBoard.getImage().getFilePath());
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
