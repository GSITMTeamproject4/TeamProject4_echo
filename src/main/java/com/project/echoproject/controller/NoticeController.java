package com.project.echoproject.controller;

import com.project.echoproject.dto.NoticeFormDTO;
import com.project.echoproject.entity.Notice;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.NoticeService;
import com.project.echoproject.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final SiteUserService siteUserService;

    // 게시글 list 전체 조회
    @GetMapping("/list")
    public String noticeList(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<Notice> noticeList = this.noticeService.getList();
        if (userDetails != null) {
            model.addAttribute("userName", userDetails.getUsername());
        }
        model.addAttribute("noticeList", noticeList);
        return "notice_list";
    }

    // 게시글 Detail 조회
    @GetMapping("/detail/{id}")
    public String noticeDetail(@PathVariable("id") Long id, Model model, Principal principal) {
        Notice notice = noticeService.getNotice(id);
        model.addAttribute("notice", notice);

        if (principal != null) {
            String currentUserId = principal.getName();
            model.addAttribute("currentUserId", currentUserId);
        } else {
            model.addAttribute("currentUserId", null);
        }
        return "notice_detail";
    }

    // 게시글 CREATE GET
    @GetMapping("/create")
    public String createNotice(NoticeFormDTO noticeFormDTO) {
        return "notice_form";
    }

    // 게시글 CREATE POST
    @PostMapping("/create")
    public String createNotice(@Valid NoticeFormDTO noticeFormDTO, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }
        String userId = principal.getName();
        SiteUser siteUser = siteUserService.findByUserId(userId);
        noticeService.create(noticeFormDTO.getTitle(), noticeFormDTO.getContent(), siteUser);
        return "redirect:/notice/list";
    }

    //MODIFY
    // 게시글 수정 GET 으로 기존 데이터 보이기
    @GetMapping("/modify/{id}")
    public String noticeModify(@PathVariable("id") Long id, Model model, Principal principal) {
        Notice notice = this.noticeService.checkNotice(id);
        if (!notice.getSiteUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        //서비스통해서 db에서 조회된 데이터 DTO에 담기
        NoticeFormDTO noticeFormDTO = new NoticeFormDTO();
        noticeFormDTO.setTitle(notice.getNotice_title());
        noticeFormDTO.setContent(notice.getNotice_content());
        //모델에 DTO와 ID를 속성값에 담아서 뷰로 보내기
        model.addAttribute("noticeFormDTO", noticeFormDTO);
        model.addAttribute("noticeId", id);

        return "notice_form";
    }

    // 게시글 수정 POST 로 수정값 넘김
    @PostMapping("/modify/{id}")
    public String noticeModify(@PathVariable("id") Long id, @Valid NoticeFormDTO noticeFormDTO, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "notice_form";
        }

        Notice notice = this.noticeService.checkNotice(id);
        if (!notice.getSiteUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        this.noticeService.modify(notice, noticeFormDTO.getTitle(), noticeFormDTO.getContent());
        return String.format("redirect:/notice/detail/%s", id);
    }

    //DELETE
    //게시글 삭제
    @GetMapping("/delete/{id}")
    //사용자 정보와 id정보받음
    public String questionDelete(Principal principal, @PathVariable("id") Long id) {
        Notice notice = this.noticeService.checkNotice(id);
        //이름확인해서 하나라도 일치하지 않으면 삭제권한없다고 띄우고
        if (!notice.getSiteUser().getUserId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        //아니면 삭제처리후 리다이렉트
        this.noticeService.delete(notice);
        return "redirect:/notice/list";
    }
}
