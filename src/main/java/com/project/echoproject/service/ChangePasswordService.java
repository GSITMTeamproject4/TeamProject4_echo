package com.project.echoproject.service;

import com.project.echoproject.dto.ChangePasswordForm;


public interface ChangePasswordService {
    void changePassword(String userId, ChangePasswordForm changePwForm);
}


