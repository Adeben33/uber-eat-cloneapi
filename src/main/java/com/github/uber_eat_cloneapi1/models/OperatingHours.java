package com.github.uber_eat_cloneapi1.models;

import jakarta.persistence.Embeddable;

@Embeddable
public class OperatingHours {

    private String openingTime;
    private String closingTime;
}
