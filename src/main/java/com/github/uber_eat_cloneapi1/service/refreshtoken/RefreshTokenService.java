package com.github.uber_eat_cloneapi1.service.refreshtoken;

import com.github.uber_eat_cloneapi1.models.RefreshTokenModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.RefreshTokenRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface RefreshTokenService {
    RefreshTokenModel createRefreshToken(String email);
    Optional<RefreshTokenModel> findByToken(String token);
    void deleteByUser(UserModel user);
    boolean isValidRefreshToken(RefreshTokenModel refreshToken);
    void invalidateRefreshToken(String token);

}
