package com.github.uber_eat_cloneapi1.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deliveries")
@Entity
public class DeliveryModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

//    @OneToMany
//    @JoinColumn(name="orders_id", nullable=false, referencedColumnName = "id")
//    private List<OrderModel> orders = new ArrayList<>();

    private String deliveryPersonName;
    private String deliveryPersonPhone;
    private String status;
    private ZonedDateTime estimatedArrival;
}
