package com.github.uber_eat_cloneapi1.controller.order;

import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @PostMapping("/orders")
    public String placeOrder(@RequestBody OrderDTO orderDTO) {
        return "Order placed successfully.";
    }

    @GetMapping("/orders/{orderId}")
    public String getOrderDetails(@PathVariable String orderId) {
        return "Details of order with ID: " + orderId;
    }

    @PutMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable String orderId) {
        return "Order with ID: " + orderId + " has been canceled.";
    }


}
