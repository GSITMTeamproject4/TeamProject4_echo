package com.project.echoproject.controller;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.ChangePasswordServiceImpl;
import com.project.echoproject.service.MypageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final ChangePasswordServiceImpl changePasswordServiceImpl;

    @Autowired
    public MypageController(MypageService mypageService, ChangePasswordServiceImpl changePasswordServiceImpl) {
        this.mypageService = mypageService;
        this.changePasswordServiceImpl = changePasswordServiceImpl;
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

    @GetMapping("/edit/{userId}/password")
    public String changeUserPassword(@PathVariable String userId, Model model) {
        SiteUser user = mypageService.getUserById(userId);
        model.addAttribute("user", user);
        model.addAttribute("changePasswordService", changePasswordServiceImpl); // ChangePasswordService 객체를 주입
        return "change_password_form";
    }

    @PostMapping("/edit/{userId}/password")
    public String processChangePassword(@PathVariable String userId,
                                        @ModelAttribute("changePasswordService") @Validated ChangePasswordServiceImpl changePasswordService,
                                        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "입력 값에 문제가 있습니다.");
            return "change_password_form";
        }

        try {
            mypageService.changePasswordForm(userId, changePasswordService);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "change_password_form";
        }

        return "redirect:/mypage/" + userId;
    }




    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable String userId, SiteUser user) {
        mypageService.deleteUser(userId, user);
        return "redirect:/";
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
