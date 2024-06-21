package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.AuthBoardRepository;
import com.project.echoproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthBoardServiceImpl implements AuthBoardService {

    private final AuthBoardRepository authBoardRepository;
    private final ImageRepository imageRepository;

    @Override
    public AuthBoard saveBoard(String title, String content, MultipartFile file, SiteUser siteUser) throws IOException {
        AuthBoard authBoard = new AuthBoard();
        authBoard.setBoardTitle(title);
        authBoard.setBoardContent(content);
        authBoard.setPostCreateDate(LocalDateTime.now());

        if (!file.isEmpty()) {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setData(file.getBytes());
            image.setUploadTime(LocalDateTime.now());
            authBoard.setImage(image);
            imageRepository.save(image); // 이미지 저장
            authBoard.setCheckImg(file.getOriginalFilename()); // checkImg 필드 설정
            authBoard.setSiteUser(siteUser); // siteUser 설정
        } else {
            throw new IOException("File is empty");
        }

        return authBoardRepository.save(authBoard);
    }

    private String encodeImageToBase64(byte[] imageData) {
        return Base64.getEncoder().encodeToString(imageData);
    }
    @Override
    public AuthBoard getAuthBoard(Long id) {
        AuthBoard authBoard = authBoardRepository.findById(id).orElse(null);
        if (authBoard != null && authBoard.getImage() != null) {
            String base64Image = encodeImageToBase64(authBoard.getImage().getData());
            authBoard.getImage().setBase64(base64Image); // Base64 문자열 설정
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
