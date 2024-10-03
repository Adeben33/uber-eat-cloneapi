package com.github.uber_eat_cloneapi1.service;


import com.github.uber_eat_cloneapi1.models.OtpModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.OtpRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPServiceImpl implements OtpService{

    private final UserRepo userRepo;
    private final OtpRepo otpRepo;
    private final JavaMailSender mailSender;


    @Autowired
    public OTPServiceImpl(UserRepo userRepo, OtpRepo otpRepo, JavaMailSender mailSender) {
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
        this.mailSender = mailSender;
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
    @Async
    // Method to send OTP email with HTML template
    public CompletableFuture<Boolean> sendOtpEmail(String toEmail, String name, String accountNumber, String otp) throws MessagingException {
        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Prepare the OTP email template
        String emailContent = getUberStyleOtpEmailTemplate(name, otp);

        // Set email details
        helper.setFrom("adeniyiebenezer33@gmail.com");
        helper.setTo(toEmail);
        helper.setSubject("Your OTP for Login");
        helper.setText(emailContent, true); // 'true' indicates HTML content

        // Send the email
        mailSender.send(message);
        System.out.println("OTP email sent successfully to " + toEmail);

        return CompletableFuture.completedFuture(true);
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


    public String getUberStyleOtpEmailTemplate(String name, String otp) {
        return "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">"
                + "<div style=\"margin:50px auto;width:70%;padding:20px 0\">"
                + "<div style=\"text-align:center;\">"
                + "<img src=\"https://upload.wikimedia.org/wikipedia/commons/c/cc/Uber_logo_2018.png\" alt=\"Uber Logo\" width=\"100\" style=\"display:block;margin:auto;\" />"
                + "</div>"
                + "<div style=\"background-color:#f9f9f9;padding:20px;border-radius:10px;\">"
                + "<h1 style=\"color:#000;font-weight:bold;text-align:center\">Your Uber verification code</h1>"
                + "<p style=\"font-size:1.2em;text-align:center\">Hi " + name + ",</p>"
                + "<p style=\"text-align:center\">To finish logging in to your Uber account, enter this verification code:</p>"
                + "<div style=\"background-color:#eee;padding:10px;border-radius:5px;text-align:center;\">"
                + "<h2 style=\"font-size:2em;color:#00466a;margin:0\">" + otp + "</h2>"
                + "</div>"
                + "<p style=\"font-size:1em;text-align:center;margin-top:20px;\">"
                + "Do not share this code with anyone. Uber staff will never ask you for this code."
                + "</p>"
                + "<p style=\"font-size:0.9em;text-align:center;color:#777\">If you didn’t make this request or you need assistance, visit our <a href=\"#\" style=\"color:#00466a;text-decoration:none;\">Help Center</a>.</p>"
                + "</div>"

                // Footer Section
                + "<div style=\"background:#000000;padding:20px;margin-top:30px;border-radius:8px;\">"
                + "<table style=\"width:100%;color:#fff;font-size:0.9em\">"
                + "<tr>"
                + "<td><a href=\"#\" style=\"color:#fff;text-decoration:none;margin-right:10px;\">Help Centre</a></td>"
                + "<td><a href=\"#\" style=\"color:#fff;text-decoration:none;margin-right:10px;\">Terms</a></td>"
                + "<td><a href=\"#\" style=\"color:#fff;text-decoration:none;margin-right:10px;\">Privacy</a></td>"
                + "<td><a href=\"#\" style=\"color:#fff;text-decoration:none;margin-right:10px;\">Email Preferences</a></td>"
                + "</tr>"
                + "</table>"
                + "<p style=\"color:#fff;margin-top:20px;\">Uber Rasier Canada Inc.<br />"
                + "121 Bloor Street East Suite #1600,<br />"
                + "Toronto, ON M4W 3M5<br />"
                + "Uber.com</p>"
                + "</div>"

                + "</div>"
                + "</div>";
    }


}
