package com.github.uber_eat_cloneapi1.dto.response;


import lombok.Data;

@Data
public class PromotionResponseDTO {
    private String promotionId;
    private String description;
    private Double discountPercentage;
    private String startDate;
    private String endDate;
}
