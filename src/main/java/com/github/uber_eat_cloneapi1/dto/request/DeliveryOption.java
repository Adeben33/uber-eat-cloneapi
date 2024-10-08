package com.github.uber_eat_cloneapi1.dto.request;

public enum DeliveryOption {
    SELF_DELIVERY,         // The restaurant handles delivery on its own
    THIRD_PARTY_DELIVERY,   // Delivery is handled by a third-party service (like Uber Eats)
    PICKUP_ONLY,            // The restaurant offers only pickup, no delivery option
    BOTH_DELIVERY_AND_PICKUP // The restaurant offers both self/third-party delivery and pickup
}

