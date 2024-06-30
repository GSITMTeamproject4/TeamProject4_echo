package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthBoardService {
    AuthBoard createBoard(String title, String content, MultipartFile file, SiteUser siteUser) throws IOException;
    AuthBoard getAuthBoard(Long id);
    List<AuthBoard> getAllBoards();
    AuthBoard getBoardById(Long id);
    String encodeImageToBase64(String filePath) throws IOException;
    AuthBoard modifyBoard(Long boardId, String title, String content, MultipartFile file, SiteUser siteUser) throws IOException;
    void deleteBoard(Long boardId, SiteUser siteUser);

    // 페이징 처리
    Page<AuthBoard> getAuthBoards(int page, int size);
}


