package com.github.uber_eat_cloneapi1.dto.response;

import lombok.Data;

@Data
public class ReviewResponseDTO {
    private String reviewId;
    private String userId;
    private String message;
    private Integer rating;
    private String restaurantId;
    private String date;
}
