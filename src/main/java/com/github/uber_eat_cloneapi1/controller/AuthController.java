package com.github.uber_eat_cloneapi1.controller;

import com.github.uber_eat_cloneapi1.dto.requests.LoginDTO;
import com.github.uber_eat_cloneapi1.models.RoleModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.dto.requests.RegisterDTO;
import com.github.uber_eat_cloneapi1.repository.RoleRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import com.github.uber_eat_cloneapi1.security.JwtGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

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

    @PostMapping("register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO) {
        if(userRepo.existsByEmail(registerDTO.getEmail())){
            return ResponseEntity.badRequest().body("Email Already Exists");
        }

        UserModel userModel = new UserModel();
        userModel.setEmail(registerDTO.getEmail());
        userModel.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        RoleModel roleModel = new RoleModel();
        roleModel.setName("USER");

        userModel.setRoles(Collections.singletonList(roleModel));
        userRepo.save(userModel);

        return ResponseEntity.ok().body(List.of(userModel, "User Register Succesfully"));
    }


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO){
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
    );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication);

        return ResponseEntity.ok(List.of("Login Successful",token));
    }

}
