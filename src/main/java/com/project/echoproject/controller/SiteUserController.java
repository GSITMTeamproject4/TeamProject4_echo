package com.project.echoproject.controller;

import com.project.echoproject.dto.SiteUserCreateForm;
import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class SiteUserController {

    private final SiteUserService siteUserService;

    @GetMapping("/signup")
    public String signup(SiteUserCreateForm siteUserCreateForm) {
        return "signupForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid SiteUserCreateForm siteUserCreateForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "signupForm";
        }

        if (!siteUserCreateForm.getPassword().equals(siteUserCreateForm.getPassword2())) {
            // 필드명, 오류코드, 오류 메시지
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signupForm";
        }


        try {
            siteUserService.create(siteUserCreateForm.getUserId(), siteUserCreateForm.getUserName(),
                    siteUserCreateForm.getPhone_num(), siteUserCreateForm.getGender(),
                    siteUserCreateForm.getPassword(), siteUserCreateForm.getEmail(),siteUserCreateForm.getImgUrl());
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            // 필드, 오류 메시지
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signupForm";
        } catch (Exception e) {
            e.printStackTrace();
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
