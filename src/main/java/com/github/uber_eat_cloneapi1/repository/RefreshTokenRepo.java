package com.github.uber_eat_cloneapi1.repository;

import com.github.uber_eat_cloneapi1.models.OtpModel;
import com.github.uber_eat_cloneapi1.models.RefreshTokenModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshTokenModel, Long> {

    Optional<RefreshTokenModel> findByUser(UserModel user);


    // Query OTP by user's email or phone number
    @Query("SELECT o FROM RefreshTokenModel o WHERE o.user.email = :identifier OR o.user.phoneNumber = :identifier")
    Optional<RefreshTokenModel> findRefreshTokenByEmailOrPhoneNumber(@Param("identifier") String identifier);



    Optional<RefreshTokenModel> findByToken(String token);

    @Transactional
    void deleteByUser(UserModel user);

}
