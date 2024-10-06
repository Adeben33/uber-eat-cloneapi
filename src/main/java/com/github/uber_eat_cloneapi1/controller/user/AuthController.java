package com.github.uber_eat_cloneapi1.controller.user;

import com.github.uber_eat_cloneapi1.dto.request.*;
import com.github.uber_eat_cloneapi1.security.JwtGenerator;
import com.github.uber_eat_cloneapi1.service.UserService.AuthServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final JwtGenerator jwtGenerator;

    private final AuthServiceImpl authServiceImpl;




    @Autowired
    public AuthController(JwtGenerator jwtGenerator, AuthServiceImpl authServiceImpl) {
        this.jwtGenerator = jwtGenerator;
        this.authServiceImpl = authServiceImpl;
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
        try {
            return authServiceImpl.registerOrSignup(registerOrLoginDTO);
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request");
        }
    }


    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        return authServiceImpl.register(registerDTO);
    }

    @PostMapping("verifyOTPAndLogin")
    public ResponseEntity<?> verifyOTPAndLogin(@RequestBody OTPDTO OTPDTO) {

            return authServiceImpl.verifyOTPAndLogin(OTPDTO);
    }


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
        return authServiceImpl.login(loginDTO);
    }


    @PostMapping("/auth/logout")
    public String logout() {
        return "User logged out successfully.";
    }

//    TODO
    @PostMapping("/auth/logout")
    public String resendOtpBySms(@RequestBody PhoneNumberDTO phoneNumberDTO) {
        return authServiceImpl.resendOtpBySms(phoneNumberDTO);
    }


    @PostMapping("/auth/logout")
    public ResponseEntity<?> resendOtpByEmail(@RequestBody EmailDTO emailDTO) {
        return authServiceImpl.resendOtpByEmail(emailDTO);
    }

    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Extract the token part after "Bearer "
        }
        return null;
    }

}
