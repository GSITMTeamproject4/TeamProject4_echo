package com.project.echoproject.email;

import com.project.echoproject.service.SiteUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/password")
public class PasswordResetController {
    private static final Logger logger = LoggerFactory.getLogger(PasswordResetController.class);


    @Autowired
    private SiteUserService siteUserService;

    @GetMapping("/find")
    public String showFindPasswordPage() {
        return "find_password";
    }

    @PostMapping("/find")
    @ResponseBody
    public ResponseEntity<?> findPassword(@RequestBody Map<String, String> request) {
        logger.info("Received password find request: {}", request);
        String userId = request.get("userId");
        String email = request.get("email");
        String userName = request.get("userName");

        boolean result = siteUserService.verifyUserAndSendResetEmail(userId, email, userName);
        if (result) {
            return ResponseEntity.ok().body(Map.of("success", true, "message", "비밀번호 재설정 이메일을 발송했습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "사용자 정보를 찾을 수 없습니다."));
        }
    }

    @GetMapping("/reset-request")
    public String showResetRequestPage(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "password_reset_request";
    }

    @PostMapping("/resend")
    @ResponseBody
    public ResponseEntity<?> resendResetEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        siteUserService.resendPasswordResetEmail(email);
        return ResponseEntity.ok().body(Map.of("message", "비밀번호 재설정 이메일을 다시 발송했습니다."));
    }

    @GetMapping("/reset")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "password_reset";
    }

    @PostMapping("/reset")
    @ResponseBody
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        boolean result = siteUserService.resetPassword(token, newPassword);
        if (result) {
            return ResponseEntity.ok().body(Map.of("success", true, "message", "비밀번호가 성공적으로 재설정되었습니다."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "비밀번호 재설정에 실패했습니다. 토큰이 유효하지 않거나 만료되었습니다."));
        }
    }

}