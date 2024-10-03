package com.github.uber_eat_cloneapi1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "otps")
@Entity
public class OtpModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String otp;

    private ZonedDateTime otpExpiryDate;

    @OneToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @CreationTimestamp
    private ZonedDateTime creationDate;

    @UpdateTimestamp
    private ZonedDateTime updateDate;

}
