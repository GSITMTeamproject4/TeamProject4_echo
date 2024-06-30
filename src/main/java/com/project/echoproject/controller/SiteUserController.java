package com.project.echoproject.controller;

import com.project.echoproject.dto.SiteUserCreateForm;
import com.project.echoproject.service.SiteUserSecurityServiceImpl;
import com.project.echoproject.service.SiteUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

// 이 컨트롤러는 "/user"로 시작하는 URL 요청을 처리합니다.
@RequestMapping("/user")
@RequiredArgsConstructor  // Lombok 어노테이션으로, final 필드들을 매개변수로 받는 생성자를 자동으로 생성합니다.
@Controller  // 이 클래스가 스프링 MVC의 컨트롤러임을 나타냅니다.
public class SiteUserController {

    private final SiteUserService siteUserService;
    private final SiteUserSecurityServiceImpl siteUserSecurityServiceImpl;

    // 회원가입 페이지를 반환하는 메서드입니다.
    @GetMapping("/signup")
    public String signup(SiteUserCreateForm siteUserCreateForm) {
        return "account/sign_up";  // 회원가입 폼 뷰를 반환합니다.
    }

    // 회원가입 폼 제출을 처리하는 메서드입니다.
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SiteUserCreateForm siteUserCreateForm,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "account/sign_up";
        }
        if (!siteUserCreateForm.getPassword1().equals(siteUserCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect",
                    "2개의 패스워드가 일치하지 않습니다.");
            return "account/sign_up";
        }
        try {
            siteUserService.create(siteUserCreateForm.getUserId(),
                    siteUserCreateForm.getUserName(),
                    siteUserCreateForm.getPhoneNum(),
                    siteUserCreateForm.getGender(),
                    siteUserCreateForm.getPassword1(),
                    siteUserCreateForm.getEmail(),
                    siteUserCreateForm.getFile(),
                    siteUserCreateForm.getZipcode(),
                    siteUserCreateForm.getStreetaddr(),
                    siteUserCreateForm.getDetailaddr());
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("userId", "userIdDuplicate", e.getMessage());
            return "account/sign_up";
        } catch (IOException e) {
            bindingResult.rejectValue("file", "fileUploadError", "파일 업로드 중 오류가 발생했습니다.");
            return "account/sign_up";
        }
        return "index";  // 성공 시 메인 페이지로 리다이렉트
    }


    // 로그인 페이지를 반환하는 메서드입니다.
    @GetMapping("/login")
    public String login() {
        return "account/login";  // 로그인 폼 뷰를 반환합니다.
    }

    // 아이디/비밀번호 찾기 페이지를 반환하는 메서드입니다.
    @GetMapping("/findlogin")
    public String showFindLogin() {
        return "findlogin";  // 아이디/비밀번호 찾기 폼 뷰를 반환합니다.
    }
}
