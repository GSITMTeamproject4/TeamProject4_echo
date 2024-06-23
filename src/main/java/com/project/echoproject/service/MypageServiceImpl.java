package com.project.echoproject.service;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public
class MypageServiceImpl implements MypageService {

    private final SiteUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MypageServiceImpl(SiteUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public SiteUser getUserById(String userId) {
        Optional<SiteUser> userInfo = userRepository.findById(userId);
        if (userInfo.isPresent()) {
            return userInfo.get();
        } else {
            throw new IllegalArgumentException("User not found");
        }
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
    public void changePasswordForm(String userId, ChangePasswordService changePasswordService) {
        Optional<SiteUser> changePwUser = userRepository.findById(userId);
        if (changePwUser.isPresent()) {
            SiteUser user = changePwUser.get();
            String storedPassword = user.getPassword();

            String currentPassword = changePasswordService.getCurrentPassword();
            String newPassword = changePasswordService.getNewPassword();
            String newPasswordConfirm = changePasswordService.getNewPasswordConfirm();

            // 입력한 현재 비밀번호와 데이터베이스의 저장된 비밀번호가 일치하는지 확인
            if (passwordEncoder.matches(currentPassword, storedPassword)) {
                // 새 비밀번호와 현재 비밀번호가 같은지 검사
                if (!currentPassword.equals(newPassword)) {
                    // 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
                    if (newPassword.equals(newPasswordConfirm)) {
                        // 비밀번호 변경
                        String encodedPassword = passwordEncoder.encode(newPassword);
                        user.setPassword(encodedPassword);
                        userRepository.save(user);
                    } else {
                        throw new IllegalArgumentException("새 비밀번호와 새 비밀번호 확인이 일치하지 않습니다.");
                    }
                } else {
                    throw new IllegalArgumentException("새 비밀번호는 현재 비밀번호와 다르게 설정되어야 합니다.");
                }
            } else {
                throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
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
