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



}
