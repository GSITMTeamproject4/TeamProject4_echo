package com.project.echoproject.controller;

import com.project.echoproject.entity.Challenge;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.ChallengeService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenge")
public class ChallengeController {
    private final ChallengeService challengeService;
    private final SiteUserService siteUserService;

    @GetMapping("")
    public String start(Model model) {
        return "challenge";
    }
    @GetMapping("/state")
    public @ResponseBody List<Map<String, Object>> monthPlan(Principal principal) {
        SiteUser siteUser = siteUserService.findByUserId(principal.getName());
            return challengeService.getChallengeList(siteUser);
    }
    @GetMapping("/add")
    public String add() {
        return "challenge_add";
    }

    @PostMapping("/add")
    public String createPost(@RequestParam("challenge_info") String challenge_info,
                             @RequestParam("content") String content,
                             @RequestParam("file") MultipartFile file,
                             Principal principal,
                             Model model) {
        try {
            String userId = principal.getName(); // 현재 로그인한 사용자의 ID를 가져옴
            SiteUser siteUser = siteUserService.findByUserId(userId); // 사용자 정보를 조회

            challengeService.createBoard(challenge_info, file, siteUser);
            return "redirect:/challenge"; // 게시판 목록으로 리다이렉트
        } catch (IOException e) {
            model.addAttribute("error", "파일 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "createAuthBoardPost";
        }
    }
}
