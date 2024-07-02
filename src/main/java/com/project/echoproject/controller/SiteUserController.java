package com.project.echoproject.controller;

import com.project.echoproject.dto.SiteUserCreateForm;
import com.project.echoproject.service.SiteUserSecurityServiceImpl;
import com.project.echoproject.service.SiteUserService;
import com.project.echoproject.service.SiteUserServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// 이 컨트롤러는 "/user"로 시작하는 URL 요청을 처리합니다.
@RequestMapping("/user")
@RequiredArgsConstructor  // Lombok 어노테이션으로, final 필드들을 매개변수로 받는 생성자를 자동으로 생성합니다.
@Controller  // 이 클래스가 스프링 MVC의 컨트롤러임을 나타냅니다.
public class SiteUserController {

    private static final Logger logger = LoggerFactory.getLogger(SiteUserServiceImpl.class);

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
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "account/sign_up";
        }

        List<String> existingProviders = siteUserService.checkEmailDuplication(siteUserCreateForm.getEmail());
        if (!existingProviders.isEmpty()) {
            model.addAttribute("error", "이미 사용 중인 이메일입니다. 다음 계정으로 등록되어 있습니다: " + String.join(", ", existingProviders));
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

    @PostMapping("/findlogin")
    public String findLogin(@RequestParam String email, Model model) {
        logger.info("Received findLogin request for email: {}", email);
        try {
            // EmailService에서 직접 이메일을 찾고 발송합니다.
            siteUserService.sendUserIdEmail(email);
            model.addAttribute("message", "이메일로 아이디를 전송했습니다.");
            logger.info("User ID email sent for: {}", email);
        } catch (RuntimeException e) {
            logger.warn("Failed to find user for email: {}", email, e);
            model.addAttribute("error", "해당 이메일로 등록된 사용자를 찾을 수 없습니다.");
        } catch (MessagingException e) {
            logger.error("Failed to send email for: {}", email, e);
            model.addAttribute("error", "이메일 전송에 실패했습니다. 잠시 후 다시 시도해주세요.");
        }
        return "findlogin";
    }

    @PostMapping("/check-email")
    @ResponseBody
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean isAvailable = siteUserService.checkEmailDuplication(email).isEmpty();
        String message = isAvailable ? "사용 가능한 이메일입니다." : "이미 가입된 이메일입니다.";
        return ResponseEntity.ok().body(Map.of("available", isAvailable, "message", message));
    }
}
