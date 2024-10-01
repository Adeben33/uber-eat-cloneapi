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
@Table(name = "reviews")
@Entity
public class ReviewModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false,referencedColumnName = "id")
    private UserModel user;

    @OneToOne
    @JoinColumn(name = "restaurant_id", nullable = false,referencedColumnName = "id")
    private RestaurantModel restaurant;

    private int rating;
    private String comment;
    private ZonedDateTime reviewDate;

}
