package com.project.echoproject.controller;

import com.project.echoproject.dto.SiteUserCreateForm;
import com.project.echoproject.entity.UserRole;
import com.project.echoproject.service.SiteUserSecurityServiceImpl;
import com.project.echoproject.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class SiteUserController {

    private final SiteUserService siteUserService;
    private final SiteUserSecurityServiceImpl siteUserSecurityServiceImpl;

    @GetMapping("/signup")
    public String signup(SiteUserCreateForm siteUserCreateForm) {
        return "signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SiteUserCreateForm siteUserCreateForm,
    BindingResult bindingResult,
    @RequestParam("profileImage") MultipartFile profileImage) {
        if (bindingResult.hasErrors()) {
            return "signupForm";
        }

        if (!siteUserCreateForm.getPassword1().equals(siteUserCreateForm.getPassword2())) {
            // 필드명, 오류코드, 오류 메시지
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signupForm";
        }

        try {
            siteUserService.create(siteUserCreateForm.getUserId(), siteUserCreateForm.getUserName(),
                    siteUserCreateForm.getPhoneNum(), siteUserCreateForm.getGender(),
                    siteUserCreateForm.getPassword1(), siteUserCreateForm.getEmail(),
                    profileImage,
                    siteUserCreateForm.getAddress());

        } catch (DataIntegrityViolationException e) {

            // 필드, 오류 메시지
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signupForm";
        } catch (Exception e) {

            bindingResult.reject("signupFailed", e.getMessage());
            return "signupForm";
        }

        return "listTest";
    }

    @GetMapping("/login")
    public String login() {
        return "loginForm";
    }



}