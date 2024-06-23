package com.project.echoproject.service;

import com.project.echoproject.dto.ChangePasswordForm;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangePasswordServiceImpl implements ChangePasswordService {

    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ChangePasswordServiceImpl(SiteUserRepository siteUserRepository, PasswordEncoder passwordEncoder) {
        this.siteUserRepository = siteUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void changePassword(String userId, ChangePasswordForm changePwForm) {
        SiteUser user = siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        // 현재 비밀번호 확인
        if (!passwordEncoder.matches(changePwForm.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
        if (!changePwForm.getNewPassword().equals(changePwForm.getNewPasswordConfirm())) {
            throw new IllegalArgumentException("새 비밀번호가 일치하지 않습니다.");
        }

        // 새 비밀번호 저장
        user.setPassword(passwordEncoder.encode(changePwForm.getNewPassword()));
        siteUserRepository.save(user);
    }
}


