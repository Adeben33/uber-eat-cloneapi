package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class PromotionDTO {
    private String description;
    private Double discountPercentage;
    private String startDate;
    private String endDate;
}
