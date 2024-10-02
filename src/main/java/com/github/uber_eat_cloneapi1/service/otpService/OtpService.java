package com.github.uber_eat_cloneapi1.service.otpService;

import java.util.concurrent.CompletableFuture;

public interface OtpService {
    String generateOTP(String accountNumber);
    CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp);
    boolean validateOTP(String accountNumber, String otp);
}
