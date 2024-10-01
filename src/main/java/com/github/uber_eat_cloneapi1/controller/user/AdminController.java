package com.github.uber_eat_cloneapi1.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController


@RequestMapping("/api/v1/admin")
public class AdminController {


    @GetMapping()
    public String AdminPing(){
        return "Admin Ping";
    }
}
