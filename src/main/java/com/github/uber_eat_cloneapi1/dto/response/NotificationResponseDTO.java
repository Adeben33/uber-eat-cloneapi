package com.github.uber_eat_cloneapi1.dto.response;

import lombok.Data;

@Data
public class NotificationResponseDTO {
    private String notificationId;
    private String message;
    private String type;  // e.g., "Order Update", "Promotion"
    private Boolean read;

}
