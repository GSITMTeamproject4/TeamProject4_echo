package com.project.echoproject.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject("비밀번호 재설정");

        String htmlContent = "<h1>비밀번호 재설정</h1>"
                + "<p>비밀번호를 재설정하려면 다음 링크를 클릭하세요:</p>"
                + "<a href='http://localhost:8080/api/password/reset?token=" + token + "'>비밀번호 재설정</a>"
                + "<br><br>";
//                + "<img src='cid:logo' alt='로고' style='width:200px;'/>";

        helper.setText(htmlContent, true);

        // 이미지 첨부 (이미지 파일은 src/main/resources 디렉토리에 있어야 함)
//        helper.addInline("logo", new ClassPathResource("static/images/logo.png"));

        mailSender.send(message);
    }
}