package com.github.uber_eat_cloneapi1.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="restaurants")
@Entity
public class RestaurantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double rating;          // Average rating of the restaurant
    private String address;
    private boolean isOpen;         // Whether the restaurant is open
    private String phoneNumber;     // Contact number for the restaurant
    private String cuisineType;     // Type of cuisine (e.g., Italian, Chinese, etc.)
    private String description;     // Short description about the restaurant
    private double deliveryFee;     // Delivery fee charged for orders

    // Operating hours of the restaurant (e.g., opening and closing times)
    @Embedded
    private OperatingHours operatingHours;

    // Menu items offered by the restaurant
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menus_id",referencedColumnName = "id")
    private List<MenuModel> menus = new ArrayList<>();

    // List of promotions the restaurant is offering
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "promotion_id", referencedColumnName = "id")
//    private List<PromotionModel> promotions = new ArrayList<>();

    // List of reviews given by customers
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "review_id", referencedColumnName = "id")
    private List<ReviewModel> reviews = new ArrayList<>();

    // Orders associated with this restaurant
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<OrderModel> orders = new ArrayList<>();

    // Delivery options (self-delivery, third-party)
    @Enumerated(EnumType.STRING)
    private DeliveryOption deliveryOption;

    // Availability status (online/offline)
    private boolean isAvailable;

//    user
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;
}
