package com.github.uber_eat_cloneapi1.dto.response;

import lombok.Data;

@Data
public class DriverPerformanceResponseDTO {
    private String driverId;
    private Double averageRating;
    private Integer totalDeliveries;
    private Double averageDeliveryTime;
}
