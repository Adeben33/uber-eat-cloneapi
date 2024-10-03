package com.github.uber_eat_cloneapi1.service.emailService;
import java.util.concurrent.CompletableFuture;


public interface EmailService {
    public CompletableFuture<Void> sendEmail(String to, String subject, String text);
    public String getOtpLoginEmailTemplate(String name,String accountNumber, String otp) ;
}

