package com.github.uber_eat_cloneapi1.controller.user;

import com.github.uber_eat_cloneapi1.dto.request.LoginDTO;
import com.github.uber_eat_cloneapi1.dto.request.RegisterOrLoginDTO;
import com.github.uber_eat_cloneapi1.models.RoleModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.dto.request.RegisterDTO;
import com.github.uber_eat_cloneapi1.repository.RoleRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import com.github.uber_eat_cloneapi1.security.JwtGenerator;
import com.github.uber_eat_cloneapi1.service.otpService.OtpService;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    // Helper method for setting up headers with authorization
    private HttpHeaders createHeaders(String token ) {
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

    @Autowired
    public AuthController(UserRepo userRepo, RoleRepo roleRepo, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                          JwtGenerator jwtGenerator, OtpService otpService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;
        this.otpService = otpService;
        ;
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getUserProfile(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        log.info("Authentication object: {}", authentication);
        log.info("Authorities: {}", authentication.getAuthorities());
        return ResponseEntity.ok("User Profile");
    }


    @PostMapping("/logiorsignup")
    public ResponseEntity<?> registerOrSignup(@RequestBody RegisterOrLoginDTO registerOrLoginDTO) {

        // Check if the user exists by email or phone number
        if (registerOrLoginDTO.getEmail() != null || registerOrLoginDTO.getPhoneNumber() != null) {

            Optional<UserModel> user = userRepo.findByEmailOrPhoneNumber(registerOrLoginDTO.getEmail(), registerOrLoginDTO.getPhoneNumber());

            // If user exists, generate and send OTP
            if (user.isPresent()) {
                Long tokenKey = 233454L;
                String token = otpService.generateOTP(tokenKey, user.get());

                if (user.get().getPhoneNumber() != null && !user.get().getPhoneNumber().isEmpty()) {
                    // TODO: Implement sendCodeToPhone logic
                    // sendCodeToPhone(user.get().getPhoneNumber());
                } else if (user.get().getEmail() != null && !user.get().getEmail().isEmpty()) {
                    // Send OTP to email
                    try {
                        String name = user.get().getFirstname() == null ?
                                "Welcome back" : user.get().getFirstname();

                        otpService.sendOtpEmail(user.get().getEmail(), name, token);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unable to send OTP");
                    }
                } else {
                    throw new IllegalArgumentException("User must have either a phone number or an email.");
                }

                // Return success response with user information
                return ResponseEntity.ok().body(List.of(user.get(), "User login successful"));
            }
            // If user doesn't exist, create new user
            else {
                UserModel newUser = new UserModel();

                // Set either email or phone number based on the provided input
                if (registerOrLoginDTO.getEmail() != null) {
                    newUser.setEmail(registerOrLoginDTO.getEmail());
                } else if (registerOrLoginDTO.getPhoneNumber() != null) {
                    newUser.setPhoneNumber(registerOrLoginDTO.getPhoneNumber());
                }

                // Set any other required fields in newUser, like name, password, etc.
                // TODO: Save newUser to database
                userRepo.save(newUser);

                // Generate OTP and send it to the new user's email or phone
                Long tokenKey = 233454L;

                String token = otpService.generateOTP(tokenKey, newUser);
                log.info(token);
                if (newUser.getPhoneNumber() != null && !newUser.getPhoneNumber().isEmpty()) {
                    // TODO: Implement sendCodeToPhone logic
                    // sendCodeToPhone(newUser.getPhoneNumber());
                } else if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
                    try {
                        otpService.sendOtpEmail(newUser.getEmail(), "New User", token);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unable to send OTP");
                    }
                }

                // Return success response with new user information
                return ResponseEntity.ok().body(List.of(newUser, "User registered successfully2"));
            }
        }

        return ResponseEntity.badRequest().body("Email or Phone Number must be provided.");
    }


//        UserModel userModel = new UserModel();
//
//        userModel.setEmail(registerDTO.getEmail());
//
//
//        RoleModel roleModel = new RoleModel();
//        roleModel.setName("ADMIN");
//
//        userModel.setRoles(Collections.singletonList(roleModel));
//        userRepo.save(userModel);
//
//


    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {

        if(userRepo.existsByEmail(registerDTO.getEmail())){
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


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
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

    @PostMapping("/auth/logout")
    public String logout() {
        return "User logged out successfully.";
    }

}
