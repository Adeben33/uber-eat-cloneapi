package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    private String status;  // e.g., "PREPARING", "DELIVERED", "CANCELLED"

}
