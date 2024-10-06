package com.github.uber_eat_cloneapi1.dto.response;

import com.github.uber_eat_cloneapi1.models.RefreshTokenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.checkerframework.checker.units.qual.N;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class JWTResponse {
    private String token;
    private String refreshToken;
}


