package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final SiteUserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

    public SiteUser getUser() {
        Optional<SiteUser> siteUser = this.userRepository.findByUserId("user1");

        return siteUser.get();
    }
}