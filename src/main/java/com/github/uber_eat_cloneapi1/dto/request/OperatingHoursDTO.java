package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class OperatingHoursDTO {
    private String day;
    private String openingTime;
    private String closingTime;

}
