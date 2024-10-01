package com.github.uber_eat_cloneapi1.dto.response;

import com.github.uber_eat_cloneapi1.models.OrderModel;
import lombok.Data;

import java.util.List;

@Data
public class OrderResponseDTO {
    private String orderId;
    private String restaurantId;
    private List<OrderModel> items;
    private String status;
    private String deliveryAddress;
    private Double totalAmount;
}
