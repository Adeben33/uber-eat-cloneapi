package com.github.uber_eat_cloneapi1.controller.order;

import com.github.uber_eat_cloneapi1.dto.request.DiscountDTO;
import com.github.uber_eat_cloneapi1.dto.request.OrderDTO;
import com.github.uber_eat_cloneapi1.dto.request.SpecialInstructionsDTO;
import com.github.uber_eat_cloneapi1.dto.request.TaxAndFeesDTO;
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

    @PutMapping("/orders/{orderId}/status")
    public String updateOrderStatus(@PathVariable String orderId, @RequestBody OrderStatusDTO orderStatusDTO) {
        return "Order status updated to: " + orderStatusDTO.getStatus() + " for order ID: " + orderId;
    }

    @PostMapping("/orders/reorder/{orderId}")
    public String reorderPreviousOrder(@PathVariable String orderId) {
            return "Order with ID: " + orderId + " has been reordered.";
    }

    @GetMapping("/orders/history")
    public String getOrderHistory() {
        return "List of past orders for the user.";
    }

    @PutMapping("/orders/{orderId}/special-instructions")
    public String addSpecialInstructions(@PathVariable String orderId, @RequestBody SpecialInstructionsDTO specialInstructionsDTO) {
            return "Special instructions added to order with ID: " + orderId;
    }

    @PostMapping("/orders/{orderId}/apply-discount")
    public String applyDiscountToOrder(@PathVariable String orderId, @RequestBody DiscountDTO discountDTO) {
        return "Discount applied to order with ID: " + orderId;
    }

    @PostMapping("/orders/{orderId}/apply-tax-fees")
            public String applyTaxAndFeesToOrder(@PathVariable String orderId, @RequestBody TaxAndFeesDTO taxAndFeesDTO) {
            return "Tax and fees applied to order with ID: " + orderId;
    }

    @GetMapping("/orders/{orderId}/track")
    public String trackOrderInRealTime(@PathVariable String orderId) {
        return "Tracking information for order with ID: " + orderId;
    }

    @GetMapping("/orders/{orderId}/estimate-time")
    public String getEstimatedDeliveryTime(@PathVariable String orderId) {
        return "Estimated delivery time for order with ID: " + orderId + " is 30-40 minutes.";
    }

    @PostMapping("/orders/{orderId}/add-tip")
    public String addTipForDriver(@PathVariable String orderId, @RequestBody TipDTO tipDTO) {
        return "Tip of " + tipDTO.getAmount() + " added for the driver for order ID: " + orderId;
    }

    @PostMapping("/orders/{orderId}/confirm-received")
            public String confirmOrderReceived(@PathVariable String orderId) {
            return "Order with ID: " + orderId + " has been confirmed as received.";
            }

    @GetMapping("/orders/active")
    public String getActiveOrders() {
        return "List of active orders for the user.";
    }

    @PostMapping("/orders/{orderId}/split-payment")
            public String splitPaymentForOrder(@PathVariable String orderId, @RequestBody SplitPaymentDTO splitPaymentDTO) {
            return "Payment for order with ID: " + orderId + " has been split.";
            }





}
