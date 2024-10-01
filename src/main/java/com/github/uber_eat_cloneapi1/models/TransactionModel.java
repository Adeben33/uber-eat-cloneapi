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
@Table(name = "stores")
@Entity
public class TransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String d;

//    @OneToMany(mappedBy = "transaction")
//    private List<OrderModel> orders = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false,referencedColumnName = "id")
    private PaymentModel payment;

    private ZonedDateTime transactionDate;
    private String status;
}
