package com.github.uber_eat_cloneapi1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stores")
@Entity
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;

    @OneToMany
    @JoinColumn(name="restaurant_id", nullable=false)
    private List<RestaurantModel> restaurant;

    @OneToMany
    @JoinColumn(name="items_id", nullable=false)
    private List<MenuModel> items;

    private double totalPrice;

    private ZonedDateTime orderTime;

    private String deliveryAddress;

    private String status;
}
