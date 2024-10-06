package com.github.uber_eat_cloneapi1.service.UserService;
import com.github.uber_eat_cloneapi1.dto.request.*;
import com.github.uber_eat_cloneapi1.models.OtpModel;
import com.github.uber_eat_cloneapi1.models.RoleModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.OtpRepo;
import com.github.uber_eat_cloneapi1.repository.RoleRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import com.github.uber_eat_cloneapi1.security.JwtGenerator;
import com.github.uber_eat_cloneapi1.service.otpService.OtpService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;





@Service
public class AuthServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final OtpRepo otpRepo;

    // Helper method for setting up headers with authorization
    private HttpHeaders createHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json");
        return headers;
    }


    private final UserRepo userRepo;
    private RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final OtpService otpService;


    @Value("${app.sms.enabled}")
    private boolean smsEnabled;

    @Value("${app.email.enabled}")
    private boolean emailEnabled;


    public AuthServiceImpl(UserRepo userRepo, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator, OtpService otpService, OtpRepo otpRepo) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.otpService = otpService;
        this.otpRepo = otpRepo;
    }

    public ResponseEntity<?> registerOrSignup(RegisterOrLoginDTO registerOrLoginDTO) throws MessagingException {

        if (registerOrLoginDTO.getPhoneNumber() == null || registerOrLoginDTO.getEmail() == null) {
            return ResponseEntity.badRequest().body("Email or Phone Number must be provided.");
        }

        Optional<UserModel> user = userRepo.findByEmailOrPhoneNumber(registerOrLoginDTO.getEmail(), registerOrLoginDTO.getPhoneNumber());

        // If user exists, generate and send OTP
        if (user.isPresent()) {
            String token = generateAndSendOtp(user.get());
            return ResponseEntity.ok().body(List.of(user.get(), "User login successful"));
        } else {
            // Create a new user

            UserModel newUser = createUser(registerOrLoginDTO);
            String token = generateAndSendOtp(newUser);
            return ResponseEntity.ok().body(List.of(newUser, "User registered successfully"));
        }
    }

    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtGenerator.generateToken(authentication);

            return ResponseEntity.ok().headers(createHeaders(token)).body(List.of("Login Successful", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    }

    public ResponseEntity<?> logout() {
        return null;
    }

    public ResponseEntity<?> register(RegisterDTO registerDTO) {

        if (userRepo.existsByEmail(registerDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email Already Exists");
        }

        UserModel userModel = new UserModel();
        userModel.setEmail(registerDTO.getEmail());
        userModel.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        RoleModel roleModel = new RoleModel();
        roleModel.setName("ADMIN");

        userModel.setRoles(Collections.singletonList(roleModel));
        userRepo.save(userModel);

        return ResponseEntity.ok().body(List.of(userModel, "User Register Succesfully"));
    }


    private UserModel createUser(RegisterOrLoginDTO registerOrLoginDTO) {
        UserModel newUser = new UserModel();


        if (registerOrLoginDTO.getEmail() != null) {
            newUser.setEmail(registerOrLoginDTO.getEmail());
        }
        if (registerOrLoginDTO.getPhoneNumber() != null) {
            newUser.setPhoneNumber(registerOrLoginDTO.getPhoneNumber());
        }

        RoleModel roleModel = new RoleModel();
        roleModel.setName("ADMIN");

        newUser.setRoles(new ArrayList<>(Collections.singletonList(roleModel)));;

        userRepo.save(newUser);
        return newUser;
    }

//    private String generateAndSendOtp(UserModel user) throws MessagingException {
//        Long tokenKey = 233454L;
//        String token = otpService.generateOTP(tokenKey, user);
//
//        if (smsEnabled && user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
//            otpService.sendOtpSMS1(user.getPhoneNumber(), token);
//        } else if (emailEnabled && user.getEmail() != null && !user.getEmail().isEmpty()) {
//            String name = user.getFirstname() == null ? "Welcome back" : user.getFirstname();
//            otpService.sendOtpEmail(user.getEmail(), name, token);
//        } else {
//            throw new IllegalArgumentException("User must have either a phone number or an email.");
//        }
//        return token;
//    }

    public ResponseEntity<?> verifyOTPAndLogin(OTPDTO otpdto) {

        Optional<UserModel> user = userRepo.findByEmailOrPhoneNumber(otpdto.getEmail(), otpdto.getPhoneNumber());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not found");
        }


        if (otpService.validateOTP(otpdto.getOtp(), user.get())) {

            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.get().getEmail(), otpdto.getOtp())
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String token = jwtGenerator.generateToken(authentication);

                return ResponseEntity.ok().headers(createHeaders(token)).body(List.of("Login Successful", token));
            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
            }

        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Otp not valid");
    }

    private String generateAndSendOtpByEmail(UserModel user) throws MessagingException {
        if (emailEnabled && user.getEmail() != null && !user.getEmail().isEmpty()) {
            Long tokenKey = 233454L;
            String token = otpService.generateOTP(tokenKey, user);

            String name = user.getFirstname() == null ? "Welcome back" : user.getFirstname();
            otpService.sendOtpEmail(user.getEmail(), name, token);

            return token;  // Return the generated OTP
        } else {
            throw new IllegalArgumentException("Email service is not enabled or user does not have a valid email.");
        }
    }

    private String generateAndSendOtpByPhoneNumber(UserModel user) throws MessagingException {
        if (smsEnabled && user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            Long tokenKey = 233454L;
            String token = otpService.generateOTP(tokenKey, user);

            otpService.sendOtpSMS1(user.getPhoneNumber(), token);

            return token;  // Return the generated OTP
        } else {
            throw new IllegalArgumentException("Phone service is not enabled or user does not have a valid phone number.");
        }
    }

    private String generateAndSendOtp(UserModel user) throws MessagingException {
        if (smsEnabled && user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            return generateAndSendOtpByPhoneNumber(user);
        } else if (emailEnabled && user.getEmail() != null && !user.getEmail().isEmpty()) {
            return generateAndSendOtpByEmail(user);
        } else {
            throw new IllegalArgumentException("User must have either a valid phone number or an email.");
        }
    }


    public ResponseEntity<?> resendOtpByEmail(EmailDTO emailDTO)  {

        UserModel user = userRepo.findByEmail(emailDTO.getEmail()).get();
        if (userRepo.findByEmail(emailDTO.getEmail()).isEmpty()){
            return ResponseEntity.badRequest().body("user cannot be found");
        };

        Optional<OtpModel> otp = otpRepo.findOtpByEmailOrPhoneNumber(emailDTO.getEmail());
        if (otp.isEmpty()) {
//            create a new otp
            try {
                generateAndSendOtpByEmail(user);
                ResponseEntity.ok().body("Otp generated and sent to email successfully");
            } catch (MessagingException e) {
                ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            if(otpService.validateOTP(otp.get().getOtp(),user)){
               try {
                   otpService.sendOtpEmail(user.getEmail(),user.getFirstname(),otp.get().getOtp());
                   ResponseEntity.ok().body("Otp generated and sent to email successfully");
               } catch (MessagingException e) {

                   ResponseEntity.badRequest().body(e.getMessage());
               }
            }else {
                try {
                    generateAndSendOtpByEmail(user);
                    ResponseEntity.ok().body("Otp generated and sent to email successfully");
                } catch (MessagingException e) {
                    ResponseEntity.badRequest().body(e.getMessage());
                }
            };
        }
        return ResponseEntity.badRequest().body("Otp not generated");
    }

    public ResponseEntity<String> resendOtpBySms(PhoneNumberDTO phoneNumberDTO) {

        UserModel user = userRepo.findByEmail(phoneNumberDTO.getPhoneNumber()).get();

        if (userRepo.findByEmail(phoneNumberDTO.getPhoneNumber()).isEmpty()){
            return ResponseEntity.badRequest().body("user cannot be found");
        };

        Optional<OtpModel> otp = otpRepo.findOtpByEmailOrPhoneNumber(phoneNumberDTO.getPhoneNumber());
        if (otp.isEmpty()) {
//            create a new otp
            try {
                generateAndSendOtpByEmail(user);
                ResponseEntity.ok().body("Otp generated and sent to email successfully");
            } catch (MessagingException e) {
                ResponseEntity.badRequest().body(e.getMessage());
            }
        } else {
            if(otpService.validateOTP(otp.get().getOtp(),user)){
                try {
                    otpService.sendOtpSMS(phoneNumberDTO.getPhoneNumber(),otp.get().getOtp());

                    ResponseEntity.ok().body("Otp generated and sent to email successfully");

                } catch (MessagingException e) {

                    ResponseEntity.badRequest().body(e.getMessage());
                }
            }else {
                try {
                    generateAndSendOtpByPhoneNumber(user);
                    ResponseEntity.ok().body("Otp generated and sent to email successfully");
                } catch (MessagingException e) {
                    ResponseEntity.badRequest().body(e.getMessage());
                }
            };
        }
        return ResponseEntity.badRequest().body("Otp not generated");
    }
}


