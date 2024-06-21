package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MypageService {

    private final SiteUserRepository userRepository;

    @Autowired
    public MypageService(SiteUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SiteUser getUserById(String userId) {
        Optional<SiteUser> userInfo = userRepository.findById(userId);
        if (userInfo.isPresent()) {
            return userInfo.get();
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    public void updateUser(String userId, SiteUser updatedUser) {
        Optional<SiteUser> userUpdate = userRepository.findById(userId);
        if (userUpdate.isPresent()) {
            SiteUser updateInfo = userUpdate.get();
            updateInfo.setUserName(updatedUser.getUserName());
            updateInfo.setPassword(updatedUser.getPassword());
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

    public void deleteUser(String userId) {
        this.userRepository.deleteById(userId);
    }


}