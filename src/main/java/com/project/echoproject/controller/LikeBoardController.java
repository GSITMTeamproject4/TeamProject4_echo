package com.project.echoproject.controller;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.LikeBoardService;
import com.project.echoproject.service.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class LikeBoardController {

    private final LikeBoardService likeBoardService;
    private final SiteUserService siteUserService;

    @GetMapping("/likeBoard/count/{boardId}")
    public ResponseEntity<Map<String, Object>> getLikeCount(@PathVariable Long boardId) {
        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeBoardService.getLikeCount(boardId));
        return ResponseEntity.ok(result);
    }

    @PostMapping("/likeBoard/toggle/{boardId}")
    public ResponseEntity<?> toggleLike(@PathVariable Long boardId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        SiteUser siteUser = siteUserService.findByUserId(principal.getName());
        likeBoardService.toggleLike(boardId, siteUser);

        Map<String, Object> result = new HashMap<>();
        result.put("likeCount", likeBoardService.getLikeCount(boardId));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/likeBoard/status/{boardId}")
    public ResponseEntity<?> getLikeStatus(@PathVariable Long boardId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        SiteUser siteUser = siteUserService.findByUserId(principal.getName());
        boolean isLiked = likeBoardService.isLikedByUser(boardId, siteUser);

        Map<String, Object> result = new HashMap<>();
        result.put("isLiked", isLiked);
        return ResponseEntity.ok(result);
    }
}