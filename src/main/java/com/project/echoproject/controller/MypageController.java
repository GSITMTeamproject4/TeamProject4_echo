package com.project.echoproject.controller;

import com.project.echoproject.dto.ChangePasswordForm;
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

    // 비밀번호 변경 폼을 표시하는 메소드
    @GetMapping("/change-password/{userId}")
    public String changePasswordPage(@PathVariable String userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "changepw_form";
    }


    // 비밀번호 변경 요청을 처리하는 메소드
    @PostMapping("/change-password/{userId}")
    public String changePassword(@PathVariable String userId,
                                 @ModelAttribute("changePasswordForm") @Validated ChangePasswordForm changePwForm,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("userId", userId);
            return "changepw_form";
        }

        try {
            changePasswordServiceImpl.changePassword(userId, changePwForm);
        } catch (IllegalArgumentException e) {
            result.rejectValue("currentPassword", "error.form", e.getMessage());
            model.addAttribute("userId", userId);
            return "changepw_form";
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
