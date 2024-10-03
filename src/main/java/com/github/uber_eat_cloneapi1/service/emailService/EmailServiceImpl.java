package com.github.uber_eat_cloneapi1.service.emailService;


import java.util.concurrent.CompletableFuture;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailServiceImpl implements EmailService {


    @Override
    public CompletableFuture<Void> sendEmail(String to, String subject, String text) {
        return null;
    }

    @Override
    public void getUberStyleOtpEmailTemplate(String name, String otp) {
        return ;
    }

}
