package com.project.echoproject.service;

import com.project.echoproject.dto.SiteUserEditForm;
import com.project.echoproject.entity.SiteUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MypageService {
    SiteUser getUserById(String userId);
    void updateUser(String userId, SiteUserEditForm updatedUser, MultipartFile file) throws IOException;
    void deleteUser(String userId);
    String encodeImageToBase64(String filePath) throws IOException;
}

