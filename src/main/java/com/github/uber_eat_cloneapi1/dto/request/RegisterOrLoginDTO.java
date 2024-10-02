package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class RegisterOrLoginDTO {
    private String email;
    private String phoneNumber;
}
