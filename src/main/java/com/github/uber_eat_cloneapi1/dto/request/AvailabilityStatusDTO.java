package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class AvailabilityStatusDTO {
    private String status;  // e.g., "OPEN", "CLOSED", "BUSY"
}