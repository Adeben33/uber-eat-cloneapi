package com.github.uber_eat_cloneapi1.dto.response;

public class TransactionResponseDTO {
    private String transactionId;
    private String orderId;
    private Double amount;
    private String type;  // e.g., "Payment", "Refund"
    private String status;
    private String date;
}

