package com.github.uber_eat_cloneapi1.controller.restaurant;

import com.github.uber_eat_cloneapi1.dto.request.MenuItemDTO;
import com.github.uber_eat_cloneapi1.dto.request.ReviewDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user/")
@PreAuthorize("hasRole('ROLE_USER')")
public class User {

    @GetMapping("/restaurants/{restaurantId}")
    public String getRestaurantDetails(@PathVariable String restaurantId) {
        return "Details of restaurant with ID: " + restaurantId;
    }

    @GetMapping("/restaurants")
    public String getRestaurants() {
        return "List of all restaurants.";
    }


    @PostMapping("/reviews/restaurant/{restaurantId}")
    public String submitRestaurantReview(@PathVariable String restaurantId, @RequestBody ReviewDTO reviewDTO) {
        return "Review submitted for restaurant with ID: " + restaurantId;
    }


    @GetMapping("/restaurants/{restaurantId}/menu")
    public String getRestaurantMenu(@PathVariable String restaurantId) {
        return "Menu for restaurant with ID: " + restaurantId;
    }

    @GetMapping("/reviews/restaurant/{restaurantId}")
    public String getRestaurantReviews(@PathVariable String restaurantId) {
        return "List of reviews for restaurant with ID: " + restaurantId;
    }


    @GetMapping("/restaurants/{restaurantId}/promotions")
    public String getRestaurantPromotions(@PathVariable String restaurantId) {
        return "List of promotions for restaurant with ID: " + restaurantId;

    }

    @GetMapping("/restaurants/{restaurantId}/active-orders")
    public String getActiveOrdersForRestaurant(@PathVariable String restaurantId) {
        return "List of active orders for restaurant with ID: " + restaurantId;
    }


//    @GetMapping("/restaurants/{restaurantId}/menu")
//    public String getAllMenuItems(@PathVariable String restaurantId) {
//        return "List of menu items for restaurant with ID: " + restaurantId;
//    }

}
