package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class PaymentDTO {
    private String cardNumber;
    private String cardholderName;
    private String expirationDate;
    private String cvv;
}
