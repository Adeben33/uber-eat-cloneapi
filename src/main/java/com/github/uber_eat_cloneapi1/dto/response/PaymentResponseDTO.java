package com.github.uber_eat_cloneapi1.dto.response;

import lombok.Data;

@Data
public class PaymentResponseDTO {
    private String paymentId;
    private String orderId;
    private Double amount;
    private String currency;
    private String paymentStatus;
}
