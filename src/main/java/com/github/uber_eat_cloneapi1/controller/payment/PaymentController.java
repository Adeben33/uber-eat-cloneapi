package com.github.uber_eat_cloneapi1.controller.payment;

import org.springframework.web.bind.annotation.*;


public class PaymentController {

    @PostMapping("/payments/add-method")
    public String addPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        return "Payment method added.";
    }


    @PostMapping("/payments/charge")
    public String chargePaymentMethod(@RequestBody PaymentDTO paymentDTO) {
        return "Payment successful.";
    }

    @DeleteMapping("/payments/remove-method/{paymentMethodId}")
            public String removePaymentMethod(@PathVariable String paymentMethodId) {
            return "Payment method with ID: " + paymentMethodId + " has been removed.";
            }

    @GetMapping("/payments/methods")
            public String getAllPaymentMethods() {
            return "List of all payment methods for the user.";
            }

    @PostMapping("/payments/order/{orderId}")
            public String makePaymentForOrder(@PathVariable String orderId, @RequestBody PaymentDTO paymentDTO) {
            return "Payment processed for order with ID: " + orderId;
            }

    @GetMapping("/payments/order/{orderId}/status")
            public String getPaymentStatus(@PathVariable String orderId) {
            return "Payment status for order with ID: " + orderId + " is: COMPLETED.";
            }

    @PostMapping("/payments/order/{orderId}/refund")
            public String refundPayment(@PathVariable String orderId) {
            return "Refund processed for order with ID: " + orderId;
            }

    @GetMapping("/payments/history")
            public String getPaymentHistory() {
            return "List of payment history for the user.";
            }

    @PostMapping("/payments/order/{orderId}/split")
            public String splitPaymentForOrder(@PathVariable String orderId, @RequestBody SplitPaymentDTO splitPaymentDTO) {
            return "Payment for order with ID: " + orderId + " has been split.";
            }

    @PostMapping("/payments/order/{orderId}/add-tip")
            public String addTipToPayment(@PathVariable String orderId, @RequestBody TipDTO tipDTO) {
            return "Tip of " + tipDTO.getAmount() + " added to payment for order ID: " + orderId;
            }

    @GetMapping("/payments/available-coupons")
            public String getAvailableCoupons() {
            return "List of available coupons for the user.";
            }
    @GetMapping("/payments/available-discounts")
            public String getAvailableDiscounts() {
            return "List of available discounts and promotions for the user.";
            }

    @PostMapping("/payments/validate-method")
            public String validatePaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
            return "Payment method validated successfully.";
            }

    @GetMapping("/payments/order/{orderId}/receipt")
            public String getPaymentReceipt(@PathVariable String orderId) {
            return "Receipt for order with ID: " + orderId;
            }



}
