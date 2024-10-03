package com.github.uber_eat_cloneapi1.service;

import com.github.uber_eat_cloneapi1.models.UserModel;
import jakarta.mail.MessagingException;

import java.util.concurrent.CompletableFuture;

public interface OtpService {
    String generateOTP(Long tokenKey , UserModel user);

    CompletableFuture<Boolean> sendOtpEmail(String toEmail, String name, String accountNumber, String otp) throws MessagingException;
    boolean validateOTP(String otp, UserModel user);
    String getUberStyleOtpEmailTemplate(String name, String otp);
}
