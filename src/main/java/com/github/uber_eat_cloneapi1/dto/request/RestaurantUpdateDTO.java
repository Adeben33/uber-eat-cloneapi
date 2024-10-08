package com.github.uber_eat_cloneapi1.dto.request;

import com.github.uber_eat_cloneapi1.models.DeliveryOption;
import com.github.uber_eat_cloneapi1.models.OperatingHours;
import lombok.Data;

@Data
public class RestaurantUpdateDTO {
    private String name;
    private String address;
    private boolean isAvailable;
    private DeliveryOption deliveryOption;

    private String phoneNumber;
    private String email;

    private OperatingHours operatingHours;

    public boolean getAvailable() {
        return isAvailable;
    }
}
