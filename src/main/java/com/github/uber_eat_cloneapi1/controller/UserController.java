package com.github.uber_eat_cloneapi1.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/api/v1/user")
public class UserController {



    @PreAuthorize("hasRole('USER')")
    public String userPing(){
        return "User Ping";
    }
}
