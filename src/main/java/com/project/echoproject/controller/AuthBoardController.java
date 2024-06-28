package com.project.echoproject.controller;

import com.project.echoproject.DuplicateReportException;
import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.AuthBoardService;
import com.project.echoproject.service.ReportBoardService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/authBoard")
@RequiredArgsConstructor
public class AuthBoardController {

    private final AuthBoardService authBoardService;
    private final SiteUserService siteUserService;
    private final ReportBoardService reportBoardService;

    @GetMapping("/list")
    public String authBoardList(@AuthenticationPrincipal UserDetails userDetails, Model model,
                                @RequestParam(defaultValue = "0") int page) {

        int size = 9; // 한 페이지에 보여줄 게시글 수
        Page<AuthBoard> authBoardPage = authBoardService.getAuthBoards(page, size);
        model.addAttribute("boards", authBoardPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", authBoardPage.getTotalPages());

        // 로그인한 유저 데이터 보내기
        if (userDetails != null) {
            model.addAttribute("userName", userDetails.getUsername());
        }
        return "authBoard/authBoard_list";
    }

    @GetMapping("/create")
    public String createPost() {
        return "authBoard/authBoard_create";
    }

    @PostMapping("/create")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("file") MultipartFile file,
                             Principal principal,
                             Model model) {

        try {
            if (principal == null) {
                // 인증되지 않은 경우 접근 거부 예외 발생
                return "redirect:/access_denied";
            }

            String userId = principal.getName(); // 현재 로그인한 사용자의 ID를 가져옴
            SiteUser siteUser = siteUserService.findByUserId(userId); // 사용자 정보를 조회

            authBoardService.createBoard(title, content, file, siteUser);
            return "redirect:/authBoard/list"; // 게시판 목록으로 리다이렉트
        } catch (IOException e) {
            model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "authBoard/authBoard_create";
        }
    }

    @GetMapping("/detail/{id}")
    public String authBoardDetail(@PathVariable("id") Long id, Model model, Principal principal) {
        AuthBoard authBoard = authBoardService.getAuthBoard(id);
        model.addAttribute("board", authBoard);

        List<AuthBoard> boards = authBoardService.getAllBoards();
        model.addAttribute("boards", boards);

        // principal이 null인지 확인하고, null이 아니면 사용자 ID를 가져옴
        if (principal != null) {
            String currentUserId = principal.getName();
            model.addAttribute("currentUserId", currentUserId);
        } else {
            model.addAttribute("currentUserId", null);
        }

        return "authBoard/authBoard_detail";
    }

    @GetMapping("/modify/{id}")
    public String modifyBoardForm(@PathVariable("id") Long id, Model model, Principal principal) {
        AuthBoard authBoard = authBoardService.getAuthBoard(id);
        if (authBoard == null) {
            return "postNotFound";
        }
        SiteUser siteUser = siteUserService.findByUserId(principal.getName());
        if (!authBoard.getSiteUser().equals(siteUser)) {
            return "error/access_denied"; // 권한이 없는 경우 접근 거부 페이지로 이동
        }
        model.addAttribute("board", authBoard);
        return "authBoard/authBoard_modify"; // 수정 폼 페이지로 이동
    }

    @PostMapping("/modify/{id}")
    public String modifyBoard(@PathVariable("id") Long id, @RequestParam String title, @RequestParam String content,
                              @RequestParam MultipartFile file, Principal principal) throws IOException {
        SiteUser siteUser = siteUserService.findByUserId(principal.getName());
        authBoardService.modifyBoard(id, title, content, file, siteUser);
        return "redirect:/authBoard/detail/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Long id, Principal principal) {
        SiteUser siteUser = siteUserService.findByUserId(principal.getName());
        authBoardService.deleteBoard(id, siteUser);
        return "redirect:/authBoard/list";
    }


    // 게시글 신고 페이지
    @GetMapping("/report/{id}")
    public String reportPage(@PathVariable("id") Long id, Model model, Principal principal,
                             @RequestParam(value = "alertMessage", required = false) String alertMessage) {
        if (principal == null) {
            return "redirect:/access_denied";
        }

        model.addAttribute("board", authBoardService.getBoardById(id)); // 게시글 정보를 모델에 추가

        if (alertMessage != null) {
            model.addAttribute("alertMessage", alertMessage);
        }

        return "authBoard/authBoard_report";
    }

    // 게시글 신고 처리
    @PostMapping("/report/{id}")
    public String report(@PathVariable("id") Long id,
                         @RequestParam("reason") String reason,
                         @RequestParam("reportContent") String reportContent,
                         Principal principal, Model model, RedirectAttributes redirectAttributes) {
        try {
            if (principal == null) {
                return "redirect:/access_denied";
            }

            String userId = principal.getName();
            reportBoardService.reportBoard(id, userId, reason, reportContent);
            return "redirect:/authBoard/detail/" + id;
        } catch (DuplicateReportException e) {
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage());
            return "redirect:/authBoard/report/" + id; // 신고 폼 페이지로 돌아가기
        }
    }
}