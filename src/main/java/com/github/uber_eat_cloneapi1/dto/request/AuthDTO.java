package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class AuthDTO {
    private String accessToken;
    private String tokenType="Bearer ";

    public AuthDTO(String accessToken) {
        this.accessToken = accessToken;
    }

}
