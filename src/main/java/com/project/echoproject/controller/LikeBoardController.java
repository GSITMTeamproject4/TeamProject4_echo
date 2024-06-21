package com.project.echoproject.controller;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.LikeBoardService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class LikeBoardController {

    private final LikeBoardService likeBoardService;
    private final SiteUserService siteUserService; // 유저 서비스 주입

    @GetMapping("/likeBoard/count/{boardId}")
    public Map<String, Object> getLikeCount(@PathVariable Long boardId) {
        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeBoardService.getLikeCount(boardId));
        return result;
    }

    @PostMapping("/likeBoard/toggle/{boardId}")
    public Map<String, Object> toggleLike(@PathVariable Long boardId, Principal principal) {
        // 로그인 구현 안되어있어서 임시로 만든 것-------
        if (principal == null) {
            principal = new Principal() {
                @Override
                public String getName() {
                    return "test";
                }
            };
        }
        //----------------
        SiteUser siteUser = siteUserService.findByUserId(principal.getName()); // 유저 서비스로 SiteUser 조회
        likeBoardService.toggleLike(boardId, siteUser);
        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeBoardService.getLikeCount(boardId));
        return result;
    }
}
