package com.github.uber_eat_cloneapi1.dto.response;

import com.github.uber_eat_cloneapi1.models.MenuModel;
import lombok.Data;

import java.util.List;

@Data
public class RestaurantResponseDTO {
    private String restaurantId;
    private String name;
    private String address;
    private String cuisineType;
    private String phone;
    private String email;
    private List<MenuModel> menu;
}