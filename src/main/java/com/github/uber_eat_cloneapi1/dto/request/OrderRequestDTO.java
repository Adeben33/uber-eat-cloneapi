package com.github.uber_eat_cloneapi1.dto.request;

import com.github.uber_eat_cloneapi1.models.OrderModel;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequestDTO {
    private String customerId;
    private String restaurantId;
    private List<OrderModel> items;
    private String deliveryAddress;
    private String paymentMethodId;
    private String specialInstructions;

}
