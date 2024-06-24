package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;

public interface SiteUserService {
    SiteUser findByUserId(String userId);
    SiteUser create(String userId, String userName, String password, String email, String phoneNum, String gender);

    SiteUser buyCoupon(String username, Long balance);
}
