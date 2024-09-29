package com.github.uber_eat_cloneapi1.repository;

import com.github.uber_eat_cloneapi1.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<UserModel, Long> {
        Optional<UserModel> findByEmail(String email);
        Boolean existsByEmail(String email);
}
