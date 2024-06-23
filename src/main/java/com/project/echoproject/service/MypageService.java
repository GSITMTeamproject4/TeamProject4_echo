package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;

public interface MypageService {
    SiteUser getUserById(String userId);
    void updateUser(String userId, SiteUser updatedUser);
    void changePasswordForm(String userId, ChangePasswordService changePasswordService);
    void deleteUser(String userId, SiteUser deleteUser);


}
