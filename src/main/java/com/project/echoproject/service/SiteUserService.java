package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SiteUserService {
    SiteUser findByUserId(String userId);
    SiteUser create(String userId, String userName, String password, String email, String phoneNum, String gender,MultipartFile imgFile,String address) throws IOException;
    SiteUser buyCoupon(String username, Long balance);
    List<SiteUser> getAllUsers();
    void addPointByAdmin(String userId,Long point);
}
