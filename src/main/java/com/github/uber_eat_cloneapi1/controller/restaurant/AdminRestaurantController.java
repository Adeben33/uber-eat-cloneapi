package com.github.uber_eat_cloneapi1.controller.restaurant;

import com.github.uber_eat_cloneapi1.dto.request.*;
import com.github.uber_eat_cloneapi1.models.OperatingHours;
import com.github.uber_eat_cloneapi1.service.restaurantsService.AdminRestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


//an Admin is also a USER
@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminRestaurantController {

    private final AdminRestaurantService adminRestaurantService;

    public AdminRestaurantController(AdminRestaurantService adminRestaurantService) {
        this.adminRestaurantService = adminRestaurantService;
    }

    @PostMapping("/restaurants/{userid}")
    public ResponseEntity<?> createRestaurant(@RequestBody RestaurantDTO restaurantDTO, @PathVariable String userid) {
        return adminRestaurantService.createRestaurant(restaurantDTO,userid);
    }

    @PutMapping("/restaurants/{userid}/{restaurantId}")
    public ResponseEntity<?> updateRestaurant(@PathVariable String restaurantId, @PathVariable String userid, @RequestBody RestaurantUpdateDTO restaurantUpdateDTO) {
//         "Restaurant information updated for ID: " + restaurantId;
        return adminRestaurantService.updateRestaurant(restaurantId,userid,restaurantUpdateDTO);
    }

    @DeleteMapping("/restaurants/{userId}/{restaurantId}/{otpInput}")
    public ResponseEntity<String> deleteRestaurant(@PathVariable String restaurantId, @PathVariable String userId, @PathVariable String otpInput) {
        return adminRestaurantService.deleteRestaurant(restaurantId,userId,otpInput);
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
    public String setRestaurantOperatingHours(@PathVariable String restaurantId, @RequestBody OperatingHours operatingHoursDTO) {
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
