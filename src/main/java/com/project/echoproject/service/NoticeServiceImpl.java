//package com.project.echoproject.service;
//
//import com.project.echoproject.entity.Notice;
//import com.project.echoproject.repository.NoticeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Service
//public class NoticeServiceImpl implements NoticeService{
//
//    private final NoticeRepository noticeRepository;
//
//
//    //컨트롤러에서..씀
//    public Notice getNotice(Long id) {
//        Optional<Notice> notice = this.noticeRepository.findById(id);
//        if (notice.isPresent()) {
//            return notice.get();
//        }
//    }
//
//
//
//}
