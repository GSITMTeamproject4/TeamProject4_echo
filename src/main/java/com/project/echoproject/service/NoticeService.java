package com.project.echoproject.service;

import com.project.echoproject.entity.Notice;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.NoticeRepository;
import com.project.echoproject.repository.SiteUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    //게시글 list 전체 가져오기
    public List<Notice> getList() {
        return noticeRepository.findAll();
    }


    //게시글 form 작성하기 create
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





    //선택해서 불러오기
    public Notice selectNotice(Long id) {
        Optional<Notice> notice = this.noticeRepository.findById(id);
        if (notice.isPresent()) {
            return notice.get();
        }else {
            throw new IllegalArgumentException("Notice not found");
        }
    }





}
