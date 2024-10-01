package com.github.uber_eat_cloneapi1.dto.response;


import lombok.Data;

@Data
public class FeedbackResponseDTO {
        private String feedbackId;
        private String userId;
        private String message;
        private Integer rating;
        private String date;
    }

