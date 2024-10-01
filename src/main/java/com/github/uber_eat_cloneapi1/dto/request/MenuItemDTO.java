package com.github.uber_eat_cloneapi1.dto.request;

import lombok.Data;

@Data
public class MenuItemDTO {
    private String name;
    private String description;
    private Double price;
    private Boolean available;
}
