package com.project.echoproject.controller;

import com.project.echoproject.dto.NoticeFormDTO;
import com.project.echoproject.entity.Notice;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.NoticeService;
import com.project.echoproject.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final SiteUserService siteUserService;

    //게시글 list 전체보이기
    @GetMapping("/list")
    public String noticeList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Notice> noticeList = this.noticeService.getList();
        // 임시로 로그인한 유저 데이터 보내기 .. 공지사항은 필요없나.??
        if (userDetails != null) {
            model.addAttribute("userName", userDetails.getUsername());
        }
        //모델에 데이터 전달, 뷰에서 사용할 속성명 "noticeList"
        model.addAttribute("noticeList", noticeList);
        //타임리프 뷰로 반환 (뷰에서 데이터에 접근, 사용하여 html페이지를 렌더링함)
        return "notice_list";
    }

    //게시글 작성하기(작성한 내용이 데이터에 들어가도록 구현) (나중에 글쓰는 권한은 관리자만 되도록하기-> (@AuthenticationPrincipal UserDetails 권한이거사용.?)
    @GetMapping("/create")
    public String createNotice(NoticeFormDTO noticeFormDTO) {
        return "notice_form";
    }


    // 공지사항 등록란에서 작성 후에 저장할 때의 post 매핑 추가
//    @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
    @PostMapping("/create")
    public String createNotice(@RequestParam("title") String title,
                               @RequestParam("content") String content,
                                Principal principal,
                                Model model) {
            String userId = principal.getName(); // 현재 로그인한 사용자의 ID를 가져옴
            SiteUser siteUser = siteUserService.findByUserId(userId); // 사용자 정보를 조회

            noticeService.create(title, content, siteUser);
            return "redirect:/notice/list"; // 게시판 목록으로 리다이렉트

    }



    //게시글 디테일 조회
    @GetMapping("/list/detail")
    public String detail() {
        return "notice_detail";
    }



    //유저정보.. 가져와서 글쓴이 보여주기 . .?
//    private final SiteUserService siteUserService;

    //게시글 list 전체보이기
    //글쓴이 정보 가져오려면 오쏘보드의 public String authBoardList(@AuthenticationPrincipal UserDetails userDetails, 이부분 참고해서 해야함
//    @GetMapping("/list")
//    public String noticeList(Model model) {
//        List<Notice> noticeList = this.noticeService.getList();
//        //모델에 데이터 전달, 뷰에서 사용할 속성명 "noticeList"
//        model.addAttribute("noticeList", noticeList);
//        //타임리프 뷰로 반환 (뷰에서 데이터에 접근, 사용하여 html페이지를 렌더링함)
//        return "notice_list";
//    }



}
