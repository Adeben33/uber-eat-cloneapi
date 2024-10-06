package com.github.uber_eat_cloneapi1.service.refreshtoken;

import com.github.uber_eat_cloneapi1.models.RefreshTokenModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.RefreshTokenRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
    @Value("${app.jwtRefreshExpiration}")
        private long refreshTokenDurationSeconds;

        private final RefreshTokenRepo refreshTokenRepository;
        private final UserRepo userRepository;

        public RefreshTokenServiceImpl(RefreshTokenRepo refreshTokenRepository, UserRepo userRepository) {
            this.refreshTokenRepository = refreshTokenRepository;
            this.userRepository = userRepository;
        }

        // Create a new refresh token
        @Override
        public RefreshTokenModel createRefreshToken(String email) {
            RefreshTokenModel refreshToken = new RefreshTokenModel();

            refreshToken.setUser(userRepository.findByEmail(email).get());
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setExpiryDate(ZonedDateTime.now().plusSeconds(refreshTokenDurationSeconds));

            log.info(refreshToken.toString());

            refreshTokenRepository.save(refreshToken);

            return refreshToken;
        }

        // Find refresh token by token value
        @Override
        public Optional<RefreshTokenModel> findByToken(String token) {
            return refreshTokenRepository.findByToken(token);
        }

        // Delete refresh token by user
        @Override
        public void deleteByUser(UserModel user) {
            refreshTokenRepository.deleteByUser(user);
        }

        // Validate refresh token
        @Override
        public boolean isValidRefreshToken(RefreshTokenModel refreshToken) {
            return refreshToken.getExpiryDate().isAfter(ZonedDateTime.now());
        }

        // Invalidate refresh token
        @Override
        public void invalidateRefreshToken(String token) {
            refreshTokenRepository.findByToken(token).ifPresent(refreshToken -> {
                refreshTokenRepository.delete(refreshToken);
            });
        }
}