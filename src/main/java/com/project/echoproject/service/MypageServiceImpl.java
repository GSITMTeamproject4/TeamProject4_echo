package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public
class MypageServiceImpl implements MypageService {

    private final SiteUserRepository userRepository;
    private final SiteUserRepository siteUserRepository;

    @Autowired
    public MypageServiceImpl(SiteUserRepository userRepository, SiteUserRepository siteUserRepository) {
        this.userRepository = userRepository;
        this.siteUserRepository = siteUserRepository;
    }

    @Override
    public SiteUser getUserById(String userId) {
        return siteUserRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    @Override
    public void updateUser(String userId, SiteUser updatedUser) {
        Optional<SiteUser> infoUpdateUser = userRepository.findById(userId);
        if (infoUpdateUser.isPresent()) {
            SiteUser updateInfo = infoUpdateUser.get();
            updateInfo.setUserName(updatedUser.getUserName());
            updateInfo.setEmail(updatedUser.getEmail());
            updateInfo.setPhoneNum(updatedUser.getPhoneNum());

            if (updatedUser.getImgUrl() != null) {
                updateInfo.setImgUrl(updatedUser.getImgUrl());
            }

            userRepository.save(updateInfo);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void deleteUser(String userId, SiteUser deleteUser) {
        Optional<SiteUser> infoDeleteUser = userRepository.findById(userId);
        if (infoDeleteUser.isPresent()) {
            userRepository.delete(infoDeleteUser.get());
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
