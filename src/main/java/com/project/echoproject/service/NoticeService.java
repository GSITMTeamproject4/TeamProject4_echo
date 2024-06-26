package com.project.echoproject.service;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.Notice;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.NoticeRepository;
import com.project.echoproject.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    //게시글 list 전체조회
    public List<Notice> getList() {
        return noticeRepository.findAll();
    }


    //게시글 detail 조회
    public Notice getNotice(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        return notice;
    }


    //CREATE
    //게시글 form 작성
    //컨트롤러에 작성된 서비스의 create메서드 사용한후 컨트롤러에서 dto로 이 내용을 담아서 데이터 저장 후 게시글 뷰로 보냄
    public void create(String title, String content, SiteUser siteUser) {
        Notice ntc = new Notice();
        ntc.setNotice_title(title);
        ntc.setNotice_content(content);
        ntc.setCreateDate(LocalDateTime.now());
        ntc.setSiteUser(siteUser);
        //내용생성 후 레파지토리로 저장
        this.noticeRepository.save(ntc);
    }

    //MODIFY
    //수정전에 게시글이 있는지 check
    public Notice checkNotice(Long id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if(notice.isPresent()) {
            return notice.get();
        }else {
            //notice에 데이터 없으면 예외처리 클래스 실행
            throw new IllegalArgumentException("Notice not found");
        }
    }

    //게시글 modify기능: dto로 DB 수정
    // 파라미터 notice:기존 데이터/ 제목,내용: DTO로 온 바꿀값
    public void modify(Notice notice, String subject, String content) {
        notice.setNotice_title(subject); //제목 수정
        notice.setNotice_content(content); //내용 수정
        //수정일자를 현재시간으로 업데이트
        notice.setModifyDate(LocalDateTime.now());
        this.noticeRepository.save(notice);
    }

    //마이페이지코드
//    public SiteUser getUserById(String userId) {
//        return userRepository.findByUserId(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
//    }
//
//    public void updateUser(String userId, SiteUser updatedUser, MultipartFile file) throws IOException {
//        SiteUser updateInfo = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new IllegalArgumentException("User not found"));
//
//        updateInfo.setUserName(updatedUser.getUserName());
//        updateInfo.setEmail(updatedUser.getEmail());
//        updateInfo.setPhoneNum(updatedUser.getPhoneNum());
//
//        if (file != null && !file.isEmpty()) {
//            Image image = imageService.saveImage(file);
//            updateInfo.setImgUrl(image.getFilePath());
//        }
//
//        userRepository.save(updateInfo);
//    }







}