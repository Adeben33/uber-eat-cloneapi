package com.github.uber_eat_cloneapi1.service.refreshtoken;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final Map<String, String> refreshTokenStore = new HashMap<>();

    public String createRefreshToken(String username) {
        String refreshToken = UUID.randomUUID().toString();
        refreshTokenStore.put(refreshToken, username);
        return refreshToken;
    }

    public String getUsernameFromRefreshToken(String refreshToken) {
        return refreshTokenStore.get(refreshToken);
    }

    public void invalidateRefreshToken(String refreshToken) {
        refreshTokenStore.remove(refreshToken);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return refreshTokenStore.containsKey(refreshToken);
    }
}
