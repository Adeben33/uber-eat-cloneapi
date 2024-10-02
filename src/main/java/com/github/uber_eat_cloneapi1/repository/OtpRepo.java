package com.github.uber_eat_cloneapi1.repository;

import com.github.uber_eat_cloneapi1.models.OtpModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface OtpRepo extends JpaRepository<OtpModel, Integer> {
    Optional<OtpModel> findByOtp(String otp);

}
