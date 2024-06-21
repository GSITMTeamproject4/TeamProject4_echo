package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.SiteUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AuthBoardService {
    AuthBoard saveBoard(String title, String content, MultipartFile file, SiteUser siteUser) throws IOException;
    AuthBoard getAuthBoard(Long id);
    List<AuthBoard> getAllBoards();
    AuthBoard getBoardById(Long id);
}
