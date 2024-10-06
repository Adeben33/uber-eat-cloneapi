package com.github.uber_eat_cloneapi1.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JWTResponse {
    private String token;
    private String refreshToken;
}
