package com.github.uber_eat_cloneapi1.service.otpService;


import com.github.uber_eat_cloneapi1.config.InfobipConfig;
import com.github.uber_eat_cloneapi1.config.TwilloConfig;
import com.github.uber_eat_cloneapi1.controller.user.AuthController;
import com.github.uber_eat_cloneapi1.models.OtpModel;
import com.github.uber_eat_cloneapi1.models.RoleModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.OtpRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import com.infobip.*;
import com.infobip.api.*;

import com.infobip.model.SmsAdvancedTextualRequest;
import com.infobip.model.SmsDestination;
import com.infobip.model.SmsResponse;
import com.infobip.model.SmsTextualMessage;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class OTPServiceImpl implements OtpService {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UserRepo userRepo;
    private final OtpRepo otpRepo;
    private final JavaMailSender mailSender;
    private final TwilloConfig twilloConfig;
    private final InfobipConfig infobipConfig;
    private final PasswordEncoder passwordEncoder;




    @Autowired
    public OTPServiceImpl(UserRepo userRepo, OtpRepo otpRepo, JavaMailSender mailSender, TwilloConfig twilloConfig, InfobipConfig infobipConfig, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
        this.mailSender = mailSender;
        this.infobipConfig = infobipConfig;
        this.passwordEncoder = passwordEncoder;
        this.twilloConfig = twilloConfig;
    }

    @Override
    public String generateOTP(Long tokenKey, UserModel user) {
        // Generate a random 4-digit OTP
        Random random = new Random();
        String token = String.format("%04d", random.nextInt(9000) + 1000);


        // Set expiry date for the OTP
        ZonedDateTime expiryDate = ZonedDateTime.now().plusMinutes(10);

        // Create the OTP model instance
        OtpModel otp = OtpModel.builder()
                .otp(passwordEncoder.encode(token))
                .otpExpiryDate(expiryDate)
                .user(user)
                .creationDate(ZonedDateTime.now()) // Assuming you have a creation date field
                .updateDate(ZonedDateTime.now()) // Assuming you have an update date field
                .build();
        log.info("2");
        // Check if there is an existing OTP for the user based on email or phone number
       Optional<OtpModel> existingOtp = otpRepo.findOtpByEmailOrPhoneNumber(user.getEmail());

        // If no existing OTP found by email, check by phone number
        if (existingOtp.isEmpty()) {

            existingOtp = otpRepo.findOtpByEmailOrPhoneNumber(user.getPhoneNumber());
        }


        // If an OTP already exists, check if it has expired
        if (existingOtp.isPresent()) {
            // Check if the existing OTP has expired
            if (existingOtp.get().getOtpExpiryDate().isAfter(ZonedDateTime.now())) {
                log.info(existingOtp.get().getOtpExpiryDate().toString());
                // Existing OTP is still valid, log and return it

                log.info("Existing OTP is still valid: " + existingOtp.get().getOtp());

                return existingOtp.get().getOtp(); // Return existing valid OTP
            } else {
                // Existing OTP has expired; update it
                existingOtp.get().setOtp( passwordEncoder.encode(token)); // Update the OTP value
                existingOtp.get().setOtpExpiryDate(expiryDate); // Update the expiry date
                existingOtp.get().setUpdateDate(ZonedDateTime.now()); // Update the update date
                otpRepo.updateOtpForUser(existingOtp.get()); // Assuming this method exists
//                use the harsh has password
                user.setPassword(existingOtp.get().getOtp());

                userRepo.save(user);

                log.info("Updated expired OTP with new OTP: " + existingOtp.get().getOtp());
            }
        } else {
            // No existing OTP found; save the new one

            otpRepo.save(otp);

            user.setPassword(otp.getOtp());

            log.info(user.toString());

            userRepo.save(user);

            log.info(token);

            log.info("Saved new OTP: " + otp.toString());
        }

        return token; // Return the newly generated OTP
    }


    @Override
    @Async
    // Method to send OTP email with HTML template
    public CompletableFuture<Boolean> sendOtpEmail(String toEmail, String name, String otp) throws MessagingException {
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
    public Boolean sendOtpSMS(String phoneNumber, String otp) throws MessagingException {

            log.info("Preparing to send OTP SMS...");

            // Create HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // The API URL
            String url = "https://d9kv48.api.infobip.com/sms/2/text/advanced";

            // JSON body with dynamic phone number and OTP
            String jsonBody = String.format(
                    "{\"messages\":[{\"destinations\":[{\"to\":\"%s\"}],\"from\":\"InfoSMS\",\"text\":\"Your Uber code is %s. Never share this code. Reply STOP ALL to +1 *********** to unsubscribe.\"}]}",
                    phoneNumber, otp
            );

            log.info("Sending request to Infobip API...");

            // Create HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofMinutes(2)) // Set a timeout duration
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Bearer " + twilloConfig.getAuthToken()) // Replace with actual API key
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody)) // Post the JSON data
                    .build();

            try {
                // Send the request and handle the response
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                // Check if the request was successful
                if (response.statusCode() == 200) {
                    log.info("OTP SMS sent successfully. Response: {}", response.body());
                    return true;
                } else {
                    log.error("Failed to send OTP SMS. Status code: {}", response.statusCode());
                    return false;
                }
            } catch (IOException | InterruptedException e) {
                log.warn("Error occurred while sending OTP SMS: {}", e.getMessage());
                return false;
            }
        }


        @Override
        public Boolean sendOtpSMS1(String phoneNumber, String otp) throws MessagingException{

        log.info("Sending request to Infobip API...");


        ApiClient apiClient = ApiClient.forApiKey(ApiKey.from(infobipConfig.getApiKey()))
                .withBaseUrl(BaseUrl.from(infobipConfig.getBaseURL()))
                .build();

        SmsApi smsApi = new SmsApi(apiClient);
        SmsTextualMessage smsMessage = new SmsTextualMessage()
                .from("InfoSMS")
                .addDestinationsItem(new SmsDestination().to("14168805101"))
                .text("Hello World from infobip-api-java-client!");

        SmsAdvancedTextualRequest smsMessageRequest = new SmsAdvancedTextualRequest()
                .messages(List.of(smsMessage));

        try {
            SmsResponse response = smsApi.sendSmsMessage(smsMessageRequest).execute();

        } catch (ApiException apiException) {
            log.warn("unable to send OTP SMS: {}", apiException.getMessage());
            return false;
        }

        return true;
    }


    @Override
    public Boolean sendOtpSMStwillo(String phoneNumberTo, String phoneNumberFrom, String otp) throws MessagingException{
        {
            Twilio.init(twilloConfig.getAccountSid(), twilloConfig.getAuthToken());

            Message message = Message
                    .creator(
                            new PhoneNumber(phoneNumberTo),
                            new PhoneNumber(phoneNumberFrom),
                            "This is the ship that made the Kessel Run in fourteen parsecs?"
                    )
                    .create();

            System.out.println(message.getSid());
        }
        return true;
    }



    @Override
    public boolean validateOTP(String otp, UserModel user) {
        // Check if the user exists by email or phone number
        Optional<UserModel> userByEmailOrPhone = userRepo.findByEmailOrPhoneNumber(user.getEmail(), user.getPhoneNumber());

        // Check if the OTP exists in the database
        Optional<OtpModel> otpRecord = otpRepo.findOtpByEmailOrPhoneNumber(user.getEmail());

        // Ensure both user and OTP are present before proceeding
        if (userByEmailOrPhone.isPresent() && otpRecord.isPresent()) {
            UserModel foundUser = userByEmailOrPhone.get();
            OtpModel foundOtp = otpRecord.get();

            log.info(foundUser.toString());
            log.info(foundOtp.toString());

            // Check if the provided OTP (raw password) is null
            if (otp == null) {
                log.error("Provided OTP is null");
                return false;  // You may return false or throw a custom exception
            }

            // Check if the OTP has expired
            if (foundOtp.getOtpExpiryDate().isBefore(ZonedDateTime.now())) {
                log.warn("OTP has expired for user: " + foundUser.getEmail());
                return false;
            }

            // Check if the OTP belongs to the correct user (match by email or phone number)
            if (Objects.equals(foundUser.getPhoneNumber(), foundOtp.getUser().getPhoneNumber())
                    || Objects.equals(foundUser.getEmail(), foundOtp.getUser().getEmail())) {

                // Ensure that the found OTP is also not null (the hashed OTP)
                if (foundOtp.getOtp() == null) {
                    log.error("Stored hashed OTP is null for user: " + foundUser.getEmail());
                    return false;  // Handle this case, maybe the OTP record was corrupted
                }

                // Use passwordEncoder.matches() to verify the hashed OTP
                if (passwordEncoder.matches(otp, foundOtp.getOtp())) {
                    log.info("OTP validation successful for user: " + foundUser.getEmail());
                    return true;
                } else {
                    log.warn("OTP does not match for user: " + foundUser.getEmail());
                }
            } else {
                log.warn("OTP does not belong to the correct user: " + foundUser.getEmail());
            }
        } else {
            log.warn("Either user or OTP record not found.");
        }

        return false;  // Return false if OTP validation fails or user/OTP is not found
    }

    @Override
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
