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
@Table(name = "orders")
@Entity
public class OrderModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private UserModel user;

//    @ManyToOne
//    @JoinColumn(name="restaurant_id", nullable=false)
//    private RestaurantModel restaurants;
//
//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name="item_id", referencedColumnName = "id")
//    private List<MenuModel> items;

//    @ManyToOne
//    @JoinColumn(name = "transaction_id", nullable = false)
//    private TransactionModel transaction;

    private double totalPrice;

    private ZonedDateTime orderTime;

    private String deliveryAddress;

    private String status;
}
