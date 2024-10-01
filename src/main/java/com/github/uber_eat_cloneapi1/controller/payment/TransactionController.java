package com.github.uber_eat_cloneapi1.controller.payment;

import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {

    @GetMapping("/transactions")
    public String getTransactionHistory() {
        return "List of transaction history.";
    }

    @GetMapping("/transactions/{transactionId}")
    public String getTransactionDetails(@PathVariable String transactionId) {
        return "Details of transaction with ID: " + transactionId;
    }


}
