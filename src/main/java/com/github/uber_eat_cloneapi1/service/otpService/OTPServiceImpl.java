package com.github.uber_eat_cloneapi1.service.otpService;


import com.github.uber_eat_cloneapi1.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPServiceImpl implements OtpService{

    private final UserRepo userRepo;

    @Autowired
    public OTPServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public String generateOTP(String accountNumber) {
        Random random = new Random();
        return String.format("%04d", random.nextInt(9000)+1000);
    }

    @Override
    public CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp) {
        return null;
    }

    @Override
    public boolean validateOTP(String accountNumber, String otp) {

        userRepo.

        return false;
    }
}
