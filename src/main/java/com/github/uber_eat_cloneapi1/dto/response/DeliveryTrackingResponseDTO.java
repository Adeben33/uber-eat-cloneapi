package com.github.uber_eat_cloneapi1.dto.response;


import lombok.Data;

@Data
public class DeliveryTrackingResponseDTO {
    private String orderId;
    private String driverId;
    private String currentLocation;
    private String estimatedDeliveryTime;
}
