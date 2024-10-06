package com.github.uber_eat_cloneapi1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payments")
@Entity
public class RefreshTokenModel {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String token;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private UserModel user;  // Assuming you have a User entity

        @Column(nullable = false)
        private ZonedDateTime expiryDate;

        private boolean isActive;


}
