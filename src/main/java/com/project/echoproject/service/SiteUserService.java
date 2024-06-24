package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;

public interface SiteUserService {
    SiteUser findByUserId(String userId);
    SiteUser create(String userId, String userName, String password, String email, String phone_num, String gender ,String imgUrl);
    SiteUser buyCoupon(String username, Long balance);
}
