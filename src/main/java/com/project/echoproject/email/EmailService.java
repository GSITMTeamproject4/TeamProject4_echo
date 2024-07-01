package com.project.echoproject.email;

import com.project.echoproject.entity.SiteUser;
import com.project.echoproject.repository.SiteUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SiteUserRepository siteUserRepository;

    public void sendPasswordResetEmail(String to, String token, List<String> socialProviders) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("비밀번호 재설정");

        StringBuilder content = new StringBuilder();
        content.append("<h1>비밀번호 재설정</h1>");

        if (!socialProviders.isEmpty()) {
            content.append("<p>귀하는 다음 소셜 로그인 계정도 가지고 있습니다: ")
                    .append(String.join(", ", socialProviders))
                    .append("</p>");
        }

        content.append("<p>일반 로그인 계정의 비밀번호를 재설정하려면 다음 링크를 클릭하세요:</p>")
                .append("<a href='http://localhost:8080/api/password/reset?token=")
                .append(token)
                .append("'>비밀번호 재설정</a>");

        helper.setText(content.toString(), true);
        mailSender.send(message);
    }

    public void sendUserIdEmail(String email) throws MessagingException {
        logger.info("Attempting to send user ID email to: {}", email);
        List<SiteUser> users = siteUserRepository.findByEmail(email);
        if (users.isEmpty()) {
            logger.warn("No user found for email: {}", email);
            throw new RuntimeException("해당 이메일로 등록된 사용자가 없습니다.");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(email);
        helper.setSubject("아이디 찾기 결과");

        StringBuilder content = new StringBuilder();
        content.append("<h2>회원님의 계정 정보</h2>");
        content.append("<ul>");

        for (SiteUser user : users) {
            if ("local".equals(user.getProvider())) {
                content.append("<li>일반 로그인 - 아이디: ").append(user.getUserId()).append("</li>");
            } else {
                content.append("<li>소셜 로그인 - ").append(user.getProvider()).append("로 가입되어 있습니다.</li>");
            }
        }

        content.append("</ul>");

        helper.setText(content.toString(), true);
        mailSender.send(message);
        logger.info("User ID email sent successfully to: {}", email);
    }

    public void sendSocialLoginReminderEmail(String to, List<String> providers) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("소셜 로그인 안내");

        String content = String.format("귀하의 계정은 다음 소셜 로그인으로 등록되어 있습니다: %s. " +
                "해당 소셜 로그인 방식으로 로그인해 주세요.", String.join(", ", providers));

        helper.setText(content, true);
        mailSender.send(message);
    }
}