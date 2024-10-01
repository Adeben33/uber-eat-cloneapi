package com.github.uber_eat_cloneapi1.controller.restaurant;

import org.springframework.web.bind.annotation.*;

@RestController
public class restaurantsController {


    @GetMapping("/restaurants")
    public String getRestaurants() {
        return "List of all restaurants.";
    }

    @GetMapping("/restaurants/{restaurantId}")
    public String getRestaurantDetails(@PathVariable String restaurantId) {
        return "Details of restaurant with ID: " + restaurantId;
    }

    @PostMapping("/restaurants/{restaurantId}/menu")
    public String addMenuItem(@PathVariable String restaurantId, @RequestBody MenuItemDTO menuItemDTO) {
        return "Menu item added to restaurant with ID: " + restaurantId;
    }

    @GetMapping("/restaurants/{restaurantId}/menu")
    public String getRestaurantMenu(@PathVariable String restaurantId) {
        return "Menu for restaurant with ID: " + restaurantId;
    }


}
