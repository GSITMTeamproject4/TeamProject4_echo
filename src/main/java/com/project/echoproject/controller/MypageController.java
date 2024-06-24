package com.project.echoproject.controller;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;

    @Autowired
    public MypageController(MypageService mypageService) {
        this.mypageService = mypageService;
    }

    @GetMapping("/edit/{userId}")
    public String editPersonalInfo(@PathVariable String userId, Model model) {
        SiteUser user = mypageService.getUserById(userId);
        model.addAttribute("user", user);
        return "edit_form";
    }

    @PostMapping("/edit/{userId}")
    public String updatePersonalInfo(@PathVariable String userId, SiteUser user) {
        mypageService.updateUser(userId, user);
        return "redirect:/mypage/" + userId;
    }

    @GetMapping("/{userId}")
    public String myPage(@PathVariable String userId, Model model) {
        SiteUser user = mypageService.getUserById(userId);
        model.addAttribute("user", user);
        return "mypage";
    }

    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable String userId) {
        mypageService.deleteUser(userId);
        return "redirect:/"; // 임시로 홈으로 리다이렉트 처리
    }


    @GetMapping("/point")
    public String myPoint(Model model) {
        return "point_status";
    }

    @GetMapping("/challenge")
    public String myChallenge(Model model) {
        return "challenge_status";
    }

    @GetMapping("/coupon")
    public String coupon(Model model) {
        return "coupon_status";
    }

    @GetMapping("/usage")
    public String myUsage(Model model) {
        return "usage_form";
    }
}
