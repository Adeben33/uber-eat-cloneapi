package com.github.uber_eat_cloneapi1.controller.payment;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class Payment {

    @PostMapping("/payments/add-method")
    public String addPaymentMethod(@RequestBody PaymentMethodDTO paymentMethodDTO) {
        return "Payment method added.";
    }


    @PostMapping("/payments/charge")
    public String chargePaymentMethod(@RequestBody PaymentDTO paymentDTO) {
        return "Payment successful.";
    }

}
