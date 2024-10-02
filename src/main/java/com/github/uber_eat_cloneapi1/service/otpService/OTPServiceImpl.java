package com.github.uber_eat_cloneapi1.service.otpService;


import com.github.uber_eat_cloneapi1.models.OtpModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.OtpRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPServiceImpl implements OtpService{

    private final UserRepo userRepo;
    private final OtpRepo otpRepo;

    @Autowired
    public OTPServiceImpl(UserRepo userRepo, OtpRepo otpRepo) {
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
    }


    @Override
    public String generateOTP(Long tokenKey , UserModel user) {

        Random random = new Random(tokenKey);
        String token =  String.format("%04d", random.nextInt(9000)+1000);

        ZonedDateTime expiryDate = ZonedDateTime.now().plusMinutes(10);

        OtpModel otp = OtpModel.builder()
                .otp(token)
                .otpExpiryDate(expiryDate)
                .user(user)
                .build();

         return token;
    }


    @Override
    public CompletableFuture<Boolean> sendOTPByEmail(String email, String name, String accountNumber, String otp) {
        return null;
    }


    @Override
    public boolean validateOTP(String otp, UserModel user) {
        Optional<UserModel> userByEmailOrPhone = userRepo.findByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber());
        Optional<OtpModel> otpRecord = otpRepo.findByOtp(otp);

        if (userByEmailOrPhone.isPresent() && otpRecord.isPresent()) {
            UserModel foundUser = userByEmailOrPhone.get();
            OtpModel foundOtp = otpRecord.get();

            // Check if the OTP has expired
            if (foundOtp.getOtpExpiryDate().isBefore(ZonedDateTime.now())) {
                return false;
            }

            // Validate if the OTP belongs to the correct user
            if (Objects.equals(foundUser.getPhoneNumber(), foundOtp.getUser().getPhoneNumber())
                    || Objects.equals(foundUser.getEmail(), foundOtp.getUser().getEmail())) {

                // Check if the OTP matches
                return Objects.equals(otp, foundOtp.getOtp());
            }
        }

        return false;
    }

}
