package com.github.uber_eat_cloneapi1.service.otpService;

import com.github.uber_eat_cloneapi1.models.UserModel;
import jakarta.mail.MessagingException;

import java.util.concurrent.CompletableFuture;

public interface OtpService {
    String generateOTP(Long tokenKey , UserModel user);

    CompletableFuture<Boolean> sendOtpEmail(String toEmail, String name, String otp) throws MessagingException;

    Boolean sendOtpSMS(String phoneNumber, String otp) throws MessagingException;

    Boolean sendOtpSMS1(String phoneNumber, String otp) throws MessagingException;

    Boolean sendOtpSMStwillo(String phoneNumberTo, String phoneNumberFrom, String otp) throws MessagingException;

    boolean validateOTP(String otp, UserModel user);

    String getUberStyleOtpEmailTemplate(String name, String otp);

}
