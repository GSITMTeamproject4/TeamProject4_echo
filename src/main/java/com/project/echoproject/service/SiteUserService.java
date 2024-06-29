package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SiteUserService {
    SiteUser findByUserId(String userId);
    SiteUser create(String userId, String userName, String password, String email, String phoneNum, String gender,MultipartFile imgFile, String zipcode, String streetaddr, String detailaddr) throws IOException;
    SiteUser buyCoupon(String username, Long balance);
    void requestPasswordReset(String email);
    boolean resetPassword(String token, String newPassword);

    boolean verifyUserAndSendResetEmail(String userId, String email, String userName);

    void resendPasswordResetEmail(String email);

    String generateResetToken();
}
