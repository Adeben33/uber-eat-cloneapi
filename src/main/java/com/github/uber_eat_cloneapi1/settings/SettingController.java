package com.github.uber_eat_cloneapi1.settings;


import org.springframework.web.bind.annotation.*;

@RestController
public class SettingController {

    @GetMapping("/users/settings")
    public String getUserSettings() {
        return "User settings.";
    }

    @PutMapping("/users/settings")
    public String updateUserSettings(@RequestBody SettingsDTO settingsDTO) {
        return "User settings updated.";
    }



}
