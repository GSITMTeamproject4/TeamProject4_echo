package com.project.echoproject.service;

import com.project.echoproject.dto.SiteUserEditForm;
import com.project.echoproject.entity.Image;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service  // 이 클래스가 서비스 계층의 빈으로 등록되도록 표시합니다.
public class MypageServiceImpl implements MypageService {

    private final SiteUserRepository userRepository;
    private final ImageService imageService;

    @Autowired  // 생성자를 통해 의존성을 주입합니다.
    public MypageServiceImpl(SiteUserRepository userRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    // 주어진 사용자 ID로 사용자를 검색합니다.
    @Override
    public SiteUser getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }

    // 주어진 사용자 ID에 해당하는 사용자의 정보를 업데이트합니다.
    public void updateUser(String userId, SiteUser updatedUser, MultipartFile file) throws IOException {
        SiteUser updateInfo = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 사용자 정보를 업데이트합니다.
        updateInfo.setUserName(updatedUser.getUserName());
        updateInfo.setEmail(updatedUser.getEmail());
        updateInfo.setPhoneNum(updatedUser.getPhoneNum());
        updateInfo.setZipcode(updatedUser.getZipcode());
        updateInfo.setStreetaddr(updatedUser.getStreetaddr());
        updateInfo.setDetailaddr(updatedUser.getDetailaddr());

        // 파일이 비어 있지 않은 경우 프로필 이미지를 업데이트합니다.
        if (file != null && !file.isEmpty()) {
            // 기존 프로필 이미지가 있다면 삭제합니다.
            if (updateInfo.getProfileImage() != null) {
                imageService.deleteImage(updateInfo.getProfileImage());
            }

            // 새 이미지를 저장합니다.
            Image newImage = imageService.saveImage(file);
            updateInfo.setProfileImage(newImage);
            newImage.setSiteUser(updateInfo);  // 이미지 엔티티와 사용자 엔티티를 연관시킵니다.
        }

        // 업데이트된 사용자 정보를 저장합니다.
        userRepository.save(updateInfo);
    }

    @Override
    public void updateUser(String userId, SiteUserEditForm updatedUser, MultipartFile file) throws IOException {
        // userId로 사용자를 조회하고, 사용자가 존재하지 않으면 예외를 던집니다.
        SiteUser user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // SiteUserEditForm에서 SiteUser로 데이터 복사
        user.setUserName(updatedUser.getUserName()); // 사용자의 이름 업데이트
        user.setNickName(updatedUser.getNickName());
        user.setEmail(updatedUser.getEmail()); // 사용자의 이메일 업데이트
        user.setPhoneNum(updatedUser.getPhoneNum()); // 사용자의 전화번호 업데이트
        user.setZipcode(updatedUser.getZipcode()); // 사용자의 주소 업데이트
        user.setStreetaddr(updatedUser.getStreetaddr());
        user.setDetailaddr(updatedUser.getDetailaddr());

        // 새로운 파일이 제공되었는지 확인
        if (file != null && !file.isEmpty()) {
            // 이미지를 저장하고 이미지 경로를 설정합니다.
            Image image = imageService.saveImage(file);
            user.setProfileImage(image);
        }

        // 업데이트된 사용자 정보를 데이터베이스에 저장합니다.
        userRepository.save(user);
    }


    // 주어진 사용자 ID에 해당하는 사용자를 삭제합니다.
    @Transactional  // 트랜잭션 범위 내에서 실행되도록 표시합니다.
    @Override
    public void deleteUser(String userId) {
        SiteUser user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        userRepository.delete(user);  // 사용자 정보를 삭제합니다.
    }

    // 주어진 파일 경로에 해당하는 이미지를 Base64 형식으로 인코딩합니다.
    @Override
    public String encodeImageToBase64(String filePath) throws IOException {
        return imageService.encodeImageToBase64(filePath);
    }
}
