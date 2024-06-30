package com.project.echoproject.controller;

import com.project.echoproject.dto.SiteUserCreateForm;
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
        return "signupForm";  // 회원가입 폼 뷰를 반환합니다.
    }

    // 회원가입 폼 제출을 처리하는 메서드입니다.
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute SiteUserCreateForm siteUserCreateForm,
                         BindingResult bindingResult,
                         @RequestParam("profileImage") MultipartFile profileImage) {
        if (bindingResult.hasErrors()) {
            return "signupForm";  // 폼 데이터에 오류가 있을 경우, 다시 회원가입 폼을 반환합니다.
        }

        // 비밀번호 확인 필드가 일치하지 않을 경우, 오류 메시지를 추가하고 다시 폼을 반환합니다.
        if (!siteUserCreateForm.getPassword1().equals(siteUserCreateForm.getPassword2())) {
            bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
            return "signupForm";
        }

        try {
            // 서비스 계층을 통해 새 사용자 생성 요청을 처리합니다.
            siteUserService.create(siteUserCreateForm.getUserId(), siteUserCreateForm.getUserName(),
                    siteUserCreateForm.getPhoneNum(), siteUserCreateForm.getGender(),
                    siteUserCreateForm.getPassword1(), siteUserCreateForm.getEmail(),
                    profileImage,
                    siteUserCreateForm.getZipcode(),
                    siteUserCreateForm.getStreetaddr(),
                    siteUserCreateForm.getDetailaddr());
        } catch (DataIntegrityViolationException e) {
            // 데이터 무결성 위반 예외가 발생한 경우, 이미 등록된 사용자라는 오류 메시지를 추가하고 폼을 반환합니다.
            bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
            return "signupForm";
        } catch (Exception e) {
            // 기타 예외가 발생한 경우, 오류 메시지를 추가하고 폼을 반환합니다.
            bindingResult.reject("signupFailed", e.getMessage());
            return "signupForm";
        }

        // 회원가입이 성공하면 메인 페이지로 리디렉션합니다.
        return "index";
    }

    // 로그인 페이지를 반환하는 메서드입니다.
    @GetMapping("/login")
    public String login() {
        return "loginForm";  // 로그인 폼 뷰를 반환합니다.
    }

    // 아이디/비밀번호 찾기 페이지를 반환하는 메서드입니다.
    @GetMapping("/findlogin")
    public String showFindLogin() {
        return "findlogin";  // 아이디/비밀번호 찾기 폼 뷰를 반환합니다.
    }
}
