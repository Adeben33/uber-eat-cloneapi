package com.github.uber_eat_cloneapi1.controller.delivery;

import org.springframework.web.bind.annotation.*;


@RestController
public class DeliveryTracking {

    @GetMapping("/delivery/{orderId}")
    public String trackDelivery(@PathVariable String orderId) {
        return "Tracking information for order with ID: " + orderId;
    }

    @PostMapping("/delivery/{orderId}/assign-driver/{driverId}")
    public String assignDriverToOrder(@PathVariable String orderId, @PathVariable String driverId) {
        return "Driver with ID: " + driverId + " assigned to order with ID: " + orderId;
    }

    @PutMapping("/delivery/{orderId}/update-status")
    public String updateDeliveryStatus(@PathVariable String orderId, @RequestBody DeliveryStatusUpdateDTO deliveryStatusUpdateDTO) {
        return "Delivery status of order " + orderId + " updated to: " + deliveryStatusUpdateDTO.getStatus();
    }

    @GetMapping("/delivery/estimate-time")
    public String estimateDeliveryTime(@RequestParam String restaurantId, @RequestParam String deliveryAddress) {
        return "Estimated delivery time is: 30-45 minutes.";
    }

    @GetMapping("/delivery/calculate-fee")
    public String calculateDeliveryFee(@RequestParam String restaurantId, @RequestParam String deliveryAddress) {
        return "The delivery fee is: $5.00";
    }

    @GetMapping("/delivery/available-drivers")
    public String getAvailableDrivers(@RequestParam String restaurantId) {
        return "List of available drivers near restaurant with ID: " + restaurantId;
    }

    @GetMapping("/delivery/history")
    public String getDeliveryHistory() {
        return "Delivery history for user: Order 1 - Delivered on 2024-08-15, Order 2 - Delivered on 2024-09-01.";
    }

    @GetMapping("/drivers/{driverId}/delivery-history")
    public String getDriverDeliveryHistory(@PathVariable String driverId) {
        return "Driver delivery history for driver ID: " + driverId;
    }

    @PostMapping("/delivery/{orderId}/notify-customer")
    public String notifyCustomer(@PathVariable String orderId) {
        return "Customer notified for order: " + orderId;
    }

    @GetMapping("/delivery/{orderId}/driver-location")
    public String getDriverLocation(@PathVariable String orderId) {
        return "Driver's current location for order " + orderId + " is: Latitude 40.7128, Longitude -74.0060";
    }

    @PostMapping("/drivers/{driverId}/update-availability")
    public String updateDriverAvailability(@PathVariable String driverId, @RequestBody AvailabilityStatusDTO availabilityStatusDTO) {
        return "Driver availability status updated to: " + availabilityStatusDTO.getStatus();
    }


    @GetMapping("/drivers/{driverId}/performance")
    public String getDriverPerformance(@PathVariable String driverId) {
        return "Performance for driver ID " + driverId + ": 50 completed deliveries, average rating: 4.8, average delivery time: 30 minutes.";
    }


}
