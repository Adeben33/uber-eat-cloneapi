package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class OTPDTO {
    private String otp;
    private String phoneNumber;
    private String email;
}
