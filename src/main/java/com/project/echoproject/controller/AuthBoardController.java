package com.project.echoproject.controller;

import com.project.echoproject.entity.AuthBoard;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.AuthBoardService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/authBoard")
@RequiredArgsConstructor
public class AuthBoardController {

    private final AuthBoardService authBoardService;
    private final SiteUserService siteUserService;

    @GetMapping("/list")
    public String authBoardList(Model model) {
        List<AuthBoard> boards = authBoardService.getAllBoards();
        model.addAttribute("boards", boards);
        return "authBoardList";
    }

    @GetMapping("/post/new")
    public String newPostForm() {
        return "newPostForm";
    }

    @PostMapping("/post")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("content") String content,
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        try {
            // 특정 user_id를 사용하여 SiteUser 조회
            String userId = "test"; // 테스트할 user_id
            SiteUser siteUser = siteUserService.findByUserId(userId);
            authBoardService.saveBoard(title, content, file, siteUser);
            return "redirect:/authBoard/list"; // 게시판 목록으로 리다이렉트
        } catch (IOException e) {
            model.addAttribute("error", "Error uploading file: " + e.getMessage());
            return "newPostForm";
        }
    }

    @GetMapping("/post/{id}")
    public String getPost(@PathVariable Long id, Model model) {
        AuthBoard authBoard = authBoardService.getAuthBoard(id);
        if (authBoard == null) {
            return "postNotFound";
        }
        model.addAttribute("authBoard", authBoard);
        return "viewPost";
    }

    @GetMapping("/detail/{id}")
    public String authBoardDetail(@PathVariable("id") Long id, Model model) {
        AuthBoard board = authBoardService.getAuthBoard(id);
        model.addAttribute("board", board);
        return "authBoardDetail";
    }
}
