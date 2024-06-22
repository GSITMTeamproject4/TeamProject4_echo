package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SiteUserServiceImpl implements SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SiteUser findByUserId(String userId) {
        return siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));
    }

    @Override
    public SiteUser create(String userId, String userName, String phoneNum, String gender, String password, String email) {
        SiteUser siteUser = new SiteUser();
        siteUser.setUserId(userId);
        siteUser.setUserName(userName);
        siteUser.setEmail(email);
        siteUser.setPhoneNum(phoneNum);
        // Gender 값을 줄임
        if ("male".equalsIgnoreCase(gender)) {
            siteUser.setGender("M");
        } else if ("female".equalsIgnoreCase(gender)) {
            siteUser.setGender("F");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        siteUser.setPassword(passwordEncoder.encode(password));
        this.siteUserRepository.save(siteUser);
        return siteUser;
    }
}
