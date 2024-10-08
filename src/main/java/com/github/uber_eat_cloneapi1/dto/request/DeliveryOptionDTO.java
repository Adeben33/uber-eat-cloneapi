package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class DeliveryOptionDTO {

    private DeliveryOption deliveryOption;  // Enum to represent the type of delivery (SELF_DELIVERY, THIRD_PARTY_DELIVERY)
    private double deliveryFee;             // Fee for delivery, if applicable
    private boolean supportsExpressDelivery; // Indicates if the restaurant supports express delivery
    private boolean contactlessDelivery;    // Indicates if contactless delivery is an option
    private double minimumOrderAmount;
}
