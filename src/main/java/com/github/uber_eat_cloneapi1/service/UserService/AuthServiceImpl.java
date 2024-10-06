package com.github.uber_eat_cloneapi1.service.UserService;
import com.github.uber_eat_cloneapi1.dto.request.*;
import com.github.uber_eat_cloneapi1.dto.response.JWTResponse;
import com.github.uber_eat_cloneapi1.models.OtpModel;
import com.github.uber_eat_cloneapi1.models.RefreshTokenModel;
import com.github.uber_eat_cloneapi1.models.RoleModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.OtpRepo;
import com.github.uber_eat_cloneapi1.repository.RefreshTokenRepo;
import com.github.uber_eat_cloneapi1.repository.RoleRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import com.github.uber_eat_cloneapi1.security.JwtGenerator;
import com.github.uber_eat_cloneapi1.service.otpService.OtpService;
import com.github.uber_eat_cloneapi1.service.refreshtoken.RefreshTokenService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;





@Service
public class AuthServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final OtpRepo otpRepo;
//    private final OtpRepo otpRepo;

    // Helper method for setting up headers with authorization
    private HttpHeaders createHeaders(String token, String type) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(type, "Bearer " + token);
        headers.set("Content-Type", "application/json");
        return headers;
    }




    private final UserRepo userRepo;
    private RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepo refreshTokenRepo;


    @Value("${app.sms.enabled}")
    private boolean smsEnabled;

    @Value("${app.email.enabled}")
    private boolean emailEnabled;


    public AuthServiceImpl(UserRepo userRepo, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JwtGenerator jwtGenerator, OtpService otpService, RefreshTokenService refreshTokenService, RefreshTokenRepo refreshTokenRepo, OtpRepo otpRepo) {
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.otpService = otpService;
        this.otpRepo = otpRepo;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepo = refreshTokenRepo;
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

    public ResponseEntity<?> login(LoginDTO loginDTO) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtGenerator.generateToken(authentication);

            return ResponseEntity.ok().headers(createHeaders(token,"AUTHORIZATION")).body(List.of("Login Successful", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
        }
    }

    public ResponseEntity<?> logout(LogoutRequestDTO logoutRequestDTO){

            // Get the refresh token from the request
            String refreshToken = logoutRequestDTO.getRefreshToken();

            // Invalidate the refresh token (remove from DB)
            refreshTokenService.invalidateRefreshToken(refreshToken);

            return ResponseEntity.ok("Logged out successfully.");
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

    public ResponseEntity<?> verifyOTPAndLogin(OTPDTO otpdto) {

        // Check if there is an existing refresh token in the database by email or phone number
        Optional<RefreshTokenModel> refreshTokenByEmailOrPhoneNumber = refreshTokenRepo.findRefreshTokenByEmailOrPhoneNumber(otpdto.getEmail());

        if (refreshTokenByEmailOrPhoneNumber.isEmpty()) {
            refreshTokenByEmailOrPhoneNumber = refreshTokenRepo.findRefreshTokenByEmailOrPhoneNumber(otpdto.getPhoneNumber());
        }

//         If a refresh token is present and valid, return "User is already logged in."
        if (refreshTokenByEmailOrPhoneNumber.isPresent() && refreshTokenService.isValidRefreshToken(refreshTokenByEmailOrPhoneNumber.get())) {
            return ResponseEntity.ok().body("User is already logged in.");
        }



        // If no valid refresh token, find the user by email or phone number
        Optional<UserModel> user = userRepo.findByEmailOrPhoneNumber(otpdto.getEmail(), otpdto.getPhoneNumber());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // Validate the OTP
        if (otpService.validateOTP(otpdto.getOtp(), user.get())) {
            try {
                // Authenticate the user
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.get().getEmail(), otpdto.getOtp())
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generate JWT token
                String token = jwtGenerator.generateToken(authentication);

                // Create new refresh token and save it in the database
                RefreshTokenModel refreshToken = refreshTokenService.createRefreshToken(authentication.getName());

                // Create HttpHeaders and add Authorization and Refresh-Token headers
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + token);
                headers.add("Refresh-Token", refreshToken.getToken());

                // Return response with both tokens in headers
                return ResponseEntity.ok().headers(headers).body(refreshToken);

            } catch (AuthenticationException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid login credentials");
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("OTP not valid");
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


    public ResponseEntity<?> resendOtpByEmail(EmailDTO emailDTO)   {

        UserModel user = userRepo.findByEmail(emailDTO.getEmail()).get();
        if (userRepo.findByEmail(emailDTO.getEmail()).isEmpty()){
            return ResponseEntity.badRequest().body("user cannot be found");
        };

        Optional<OtpModel> otp =  otpRepo.findOtpByEmailOrPhoneNumber(emailDTO.getEmail());

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
                generateAndSendOtpByPhoneNumber(user);
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
                    ResponseEntity.ok().body("Otp generated and sent to phone number successfully");
                } catch (MessagingException e) {
                    ResponseEntity.badRequest().body(e.getMessage());
                }
            };
        }
        return ResponseEntity.badRequest().body("Otp not generated");
    }

    public ResponseEntity<?> refreshJWTtoken(RefreshTokenRequest refreshTokenRequest) {

        String requestRefreshToken = refreshTokenRequest.getRefreshToken();

        // Find the refresh token from the database
        Optional<RefreshTokenModel> refreshTokenOpt = refreshTokenService.findByToken(requestRefreshToken);

        // Check if the refresh token is valid
        if (refreshTokenOpt.isPresent() && refreshTokenService.isValidRefreshToken(refreshTokenOpt.get())) {
            // Get the user associated with the refresh token
            UserModel user = refreshTokenOpt.get().getUser();

            // Generate a new JWT based on the user's information
            String newJwt = jwtGenerator.generateToken(user.getEmail(), user.getRoles());
            // Return the new JWT and the same refresh token
            JWTResponse jwtResponse = new JWTResponse(newJwt, requestRefreshToken);

            return ResponseEntity.ok(jwtResponse);
        }

        // If the refresh token is invalid or expired, return a forbidden response
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid or expired refresh token");
    }



}


