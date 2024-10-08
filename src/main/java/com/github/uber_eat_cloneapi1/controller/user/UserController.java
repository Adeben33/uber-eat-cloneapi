package com.github.uber_eat_cloneapi1.controller.user;


import com.github.uber_eat_cloneapi1.dto.request.UserProfileDTO;
import org.springframework.web.bind.annotation.*;

@RestController
//@RequestMapping("/api/v1/user")
public class UserController {


    public String userPing(){
        return "User Ping";
    }


    @GetMapping("/users/profile")
    public String getUserProfile() {
        return "User profile details.";
    }


    @PutMapping("/users/profile")
    public String updateUserProfile(@RequestBody UserProfileDTO userProfileDTO) {
        return "User profile updated.";
    }

}
