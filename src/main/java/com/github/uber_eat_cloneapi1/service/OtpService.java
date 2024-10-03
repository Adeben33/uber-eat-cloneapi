package com.github.uber_eat_cloneapi1.service;

import com.github.uber_eat_cloneapi1.models.UserModel;

import java.util.concurrent.CompletableFuture;

public interface OtpService {
    String generateOTP(Long tokenKey , UserModel user);

    CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp);
    boolean validateOTP(String otp, UserModel user);
}
