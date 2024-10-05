package com.github.uber_eat_cloneapi1.controller.user;

import com.github.uber_eat_cloneapi1.dto.request.LoginDTO;
import com.github.uber_eat_cloneapi1.dto.request.OTPDTO;
import com.github.uber_eat_cloneapi1.dto.request.RegisterOrLoginDTO;
import com.github.uber_eat_cloneapi1.models.RoleModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.dto.request.RegisterDTO;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import com.github.uber_eat_cloneapi1.security.JwtGenerator;
import com.github.uber_eat_cloneapi1.service.UserService.AuthServiceImpl;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    private final AuthServiceImpl authServiceImpl;




    @Autowired
    public AuthController(AuthServiceImpl authServiceImpl) {
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


}
