package com.github.uber_eat_cloneapi1.service.refreshtoken;

import org.springframework.stereotype.Service;



public interface RefreshTokenService {
    public String getUsernameFromRefreshToken(String refreshToken);
    public void invalidateRefreshToken(String refreshToken);
    public boolean validateRefreshToken(String refreshToken);
    String createRefreshToken(String username);
}
