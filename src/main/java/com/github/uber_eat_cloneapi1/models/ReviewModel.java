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
@Table(name = "stores")
@Entity
public class ReviewModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private UserModel user;
    private RestaurantModel restaurant;
    private int rating;
    private String comment;
    private ZonedDateTime reviewDate;
}
