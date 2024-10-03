package com.github.uber_eat_cloneapi1.repository;

import com.github.uber_eat_cloneapi1.models.OtpModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface OtpRepo extends JpaRepository<OtpModel, Integer> {
    Optional<OtpModel> findByOtp(String otp);

    Optional<OtpModel> findByUser(UserModel user);


    // Query OTP by user's email or phone number
    @Query("SELECT o FROM OtpModel o WHERE o.user.email = :identifier OR o.user.phoneNumber = :identifier")
    OtpModel findOtpByEmailOrPhoneNumber(@Param("identifier") String identifier);

    @Modifying
    @Transactional
    default void updateOtpForUser(OtpModel otp) {
        updateOtp(otp.getOtp(), otp.getOtpExpiryDate(), otp.getUpdateDate(), otp.getId());
    }

    @Modifying
    @Transactional
    @Query("UPDATE OtpModel o SET o.otp = :otp, o.otpExpiryDate = :otpExpiryDate, o.updateDate = :updateDate WHERE o.id = :id")
    int updateOtp(@Param("otp") String otp,
                  @Param("otpExpiryDate") ZonedDateTime otpExpiryDate,
                  @Param("updateDate") ZonedDateTime updateDate,
                  @Param("id") Long id);


}
