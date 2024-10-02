package com.github.uber_eat_cloneapi1.repository;

import com.github.uber_eat_cloneapi1.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserModel, Long> {
        Optional<UserModel> findByEmail(String email);

        @Query("SELECT u FROM UserModel u WHERE (:email IS NULL OR u.email = :email) "
                + "AND (:phoneNumber IS NULL OR u.phoneNumber = :phoneNumber)")
        Optional<UserModel> findByEmailOrPhoneNumber(@Param("email") String email,
                                                     @Param("phoneNumber") String phoneNumber);

        Boolean existsByEmail(String email);
        Boolean existsByPhoneNumber(String phoneNumber);
}
