package com.github.uber_eat_cloneapi1.controller.user;

import com.github.uber_eat_cloneapi1.dto.request.LoginDTO;
import com.github.uber_eat_cloneapi1.models.RoleModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.dto.request.RegisterDTO;
import com.github.uber_eat_cloneapi1.repository.RoleRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import com.github.uber_eat_cloneapi1.security.JwtGenerator;
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


    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtGenerator jwtGenerator;

    @Autowired
    public AuthController(UserRepo userRepo, RoleRepo roleRepo, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder,
                          JwtGenerator jwtGenerator) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtGenerator = jwtGenerator;;
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
