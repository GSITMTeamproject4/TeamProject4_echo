package com.project.echoproject.service;

import com.project.echoproject.dto.SiteUserEditForm;
import com.project.echoproject.entity.SiteUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MypageService {
    SiteUser getUserById(String userId);

    // 주어진 사용자 ID에 해당하는 사용자의 정보를 업데이트합니다.
    void updateUser(String userId, SiteUser updatedUser, MultipartFile file) throws IOException;

    void updateUser(String userId, SiteUserEditForm updatedUser) throws IOException;
    void deleteUser(String userId);
    String encodeImageToBase64(String filePath) throws IOException;
}

