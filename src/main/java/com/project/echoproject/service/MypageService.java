package com.project.echoproject.service;

import com.project.echoproject.dto.ChangePasswordForm;
import com.project.echoproject.entity.SiteUser;

public interface MypageService {
    SiteUser getUserById(String userId);
    void updateUser(String userId, SiteUser updatedUser);
    void deleteUser(String userId, SiteUser deleteUser);


}
