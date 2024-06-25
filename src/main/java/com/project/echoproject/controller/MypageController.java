package com.project.echoproject.controller;

import com.project.echoproject.dto.ChangePasswordForm;
import com.project.echoproject.dto.UseAmountForm;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.entity.UseAmount;
import com.project.echoproject.service.ChangePasswordServiceImpl;
import com.project.echoproject.service.MypageService;

import com.project.echoproject.service.UseAmountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

@Controller
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;
    private final ChangePasswordServiceImpl changePasswordServiceImpl;
    private final UseAmountServiceImpl useAmountServiceImpl;

    @Autowired
    public MypageController(MypageService mypageService,
                            ChangePasswordServiceImpl changePasswordServiceImpl,
                             UseAmountServiceImpl useAmountServiceImpl) {
        this.mypageService = mypageService;
        this.changePasswordServiceImpl = changePasswordServiceImpl;
        this.useAmountServiceImpl = useAmountServiceImpl;
    }

    @GetMapping("/edit/{userId}")
    public String editPersonalInfo(@PathVariable String userId, Model model) {
        SiteUser user = mypageService.getUserById(userId);
        model.addAttribute("user", user);
        return "edit_form";
    }

    @PostMapping("/edit/{userId}")
    public String updatePersonalInfo(@PathVariable String userId,
                                     @ModelAttribute SiteUser updatedUser,
                                     @RequestParam(value = "file", required = false) MultipartFile file,
                                     Model model) {
        try {
            mypageService.updateUser(userId, updatedUser, file);
            return "redirect:/mypage/" + userId;
        } catch (IOException e) {
            model.addAttribute("errorMessage", "이미지 업로드 중 오류가 발생했습니다: " + e.getMessage());
            return "edit_form";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "edit_form";
        }
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

    @GetMapping("/input-useamount/{userId}")
    public String showUsageForm(@PathVariable String userId, Model model) {
        try {
            UseAmountForm useAmountForm = new UseAmountForm();
            model.addAttribute("useAmountForm", useAmountForm);
            model.addAttribute("userId", userId);

            LocalDate currentDate = LocalDate.now();
            model.addAttribute("currentDate", currentDate);

            return "useamount_form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/input-useamount/{userId}")
    public String processUsageForm(@PathVariable String userId,
                                   @ModelAttribute("useAmountForm") UseAmountForm useAmountForm,
                                   BindingResult bindingResult,
                                   Model model) {
        LocalDate currentDate = LocalDate.now();
        if (!useAmountForm.getUseDate().equals(currentDate)) {
            bindingResult.rejectValue("useDate", "error.useDate", "날짜는 오늘이어야 합니다.");
            model.addAttribute("userId", userId);
            return "useamount_form";
        }

        try {
            useAmountServiceImpl.saveUseAmount(userId, useAmountForm);
            return "redirect:/mypage/useamount-detail/" + userId + "?year=" + currentDate.getYear();
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userId", userId);
            return "useamount_form";
        }
    }

    @GetMapping("/useamount-detail/{userId}")
    public String showUseAmountDetail(@PathVariable String userId,
                                      @RequestParam(required = false) Integer year,
                                      Model model) {
        try {
            int currentYear = (year != null) ? year : LocalDate.now().getYear();

            Map<Integer, UseAmount> useAmounts = useAmountServiceImpl.getMonthlyUseAmounts(userId, currentYear);
            model.addAttribute("useAmounts", useAmounts);
            model.addAttribute("userId", userId);
            model.addAttribute("year", currentYear);
            return "useamount_detail";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/challenge")
    public String myChallenge(Model model) {
        return "challenge_status";
    }

    @GetMapping("/coupon")
    public String coupon(Model model) {
        return "coupon_status";
    }

}




