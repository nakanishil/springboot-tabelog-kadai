package com.example.nagoyameshi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}") // 既に設定済みの送信元アドレス
    private String fromAddress;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetMail(User user, String token) {
        String resetLink = "http://localhost:8080/password-reset/reset-password?token=" + token;
        String subject = "パスワードリセットのご案内";
        String text = "以下のリンクからパスワードリセットを行ってください。\n\n" 
            + resetLink 
            + "\n\n有効期限内にアクセスしてください。";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(user.getMailAddress());
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }
}
