package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;

public interface SiteUserService {
    SiteUser findByUserId(String userId);
}
