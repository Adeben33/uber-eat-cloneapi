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
@Table(name = "payments")
@Entity
public class PaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paymentMethod;
    private double amount;

//    @OneToMany
//    @JoinColumn(name="orders_id", nullable=false, referencedColumnName = "id")
//    private List<OrderModel> orders = new ArrayList<>();
}
