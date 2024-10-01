package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class FeedbackDTO {
    private String orderId;
    private String userId;
    private String feedbackType;  // e.g., "Restaurant", "Driver", "Order"
    private String message;
    private Integer rating;  // e.g., 1-5 stars
}
