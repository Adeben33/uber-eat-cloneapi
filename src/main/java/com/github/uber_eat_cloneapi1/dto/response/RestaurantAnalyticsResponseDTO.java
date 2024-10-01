package com.github.uber_eat_cloneapi1.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class RestaurantAnalyticsResponseDTO {
    private String restaurantId;
    private Integer totalOrders;
    private Double totalRevenue;
    private Double averageRating;
    private List<String> popularMenuItems;
}
