package com.github.uber_eat_cloneapi1.dto.response;

import lombok.Data;

@Data
public class MenuItemResponseDTO {
    private String itemId;
    private String name;
    private String description;
    private Double price;
    private Boolean available;
}
