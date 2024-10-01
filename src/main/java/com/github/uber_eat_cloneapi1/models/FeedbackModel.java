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
@Table(name = "feedbacks")
@Entity
public class FeedbackModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    private DriverModel driver;

//    @OneToOne
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
//    private OrderModel order;

    private String comment;

    private int rating; // Scale of 1-5

    private ZonedDateTime feedbackDate;

}
