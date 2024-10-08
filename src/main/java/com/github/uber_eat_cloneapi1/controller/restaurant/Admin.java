package com.github.uber_eat_cloneapi1.controller.restaurant;

import com.github.uber_eat_cloneapi1.dto.request.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


//an Admin is also a USER
@RestController
@PreAuthorize("hasRole('ROLE_Admin')")
public class Admin {

    @PostMapping("/restaurants")
    public String createRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        return "Restaurant created successfully with name: " + restaurantDTO.getName();
    }

    @PutMapping("/restaurants/{restaurantId}")
    public String updateRestaurant(@PathVariable String restaurantId, @RequestBody RestaurantUpdateDTO restaurantUpdateDTO) {
        return "Restaurant information updated for ID: " + restaurantId;
    }

    @DeleteMapping("/restaurants/{restaurantId}")
    public String deleteRestaurant(@PathVariable String restaurantId) {
        return "Restaurant with ID: " + restaurantId + " has been deleted.";
    }

    @PostMapping("/restaurants/{restaurantId}/menu")
    public String addMenuItem(@PathVariable String restaurantId, @RequestBody MenuItemDTO menuItemDTO) {
        return "Menu item added to restaurant with ID: " + restaurantId;
    }

    @DeleteMapping("/restaurants/{restaurantId}/menu/{itemId}")
    public String deleteMenuItem(@PathVariable String restaurantId, @PathVariable String itemId) {
        return "Menu item with ID: " + itemId + " deleted from restaurant with ID: " + restaurantId;
    }

    @PutMapping("/restaurants/{restaurantId}/menu/{itemId}")
    public String updateMenuItem(@PathVariable String restaurantId, @PathVariable String itemId, @RequestBody MenuItemDTO menuItemDTO) {
        return "Menu item with ID: " + itemId + " updated for restaurant with ID: " + restaurantId;
    }

    @PostMapping("/restaurants/{restaurantId}/operating-hours")
    public String setRestaurantOperatingHours(@PathVariable String restaurantId, @RequestBody OperatingHoursDTO operatingHoursDTO) {
        return "Operating hours set for restaurant with ID: " + restaurantId;
    }


    @PostMapping("/restaurants/{restaurantId}/promotion")
    public String createRestaurantPromotion(@PathVariable String restaurantId, @RequestBody PromotionDTO promotionDTO) {
        return "Promotion created for restaurant with ID: " + restaurantId;
    }

    @PutMapping("/restaurants/{restaurantId}/promotion/{promotionId}")
    public String updateRestaurantPromotion(@PathVariable String restaurantId, @PathVariable String promotionId, @RequestBody PromotionDTO promotionDTO) {
        return "Promotion with ID: " + promotionId + " updated for restaurant with ID: " + restaurantId;
    }


    @PostMapping("/restaurants/{restaurantId}/reviews/{reviewId}/respond")
    public String respondToReview(@PathVariable String restaurantId, @PathVariable String reviewId, @RequestBody ReviewResponseDTO responseDTO) {
        return "Response sent to review with ID: " + reviewId + " for restaurant with ID: " + restaurantId;
    }


    @GetMapping("/restaurants/{restaurantId}/analytics")
    public String getRestaurantPerformanceAnalytics(@PathVariable String restaurantId) {
        return "Performance analytics for restaurant with ID: " + restaurantId;
    }

    @PostMapping("/restaurants/{restaurantId}/orders/{orderId}/confirm-preparation")
    public String confirmOrderPreparation(@PathVariable String restaurantId, @PathVariable String orderId) {
        return "Order with ID: " + orderId + " is ready for delivery.";
    }

    @PostMapping("/restaurants/{restaurantId}/availability")
    public String setRestaurantAvailability(@PathVariable String restaurantId, @RequestBody AvailabilityStatusDTO statusDTO) {
        return "Availability status for restaurant with ID: " + restaurantId + " set to: " + statusDTO.getStatus();
    }


}
