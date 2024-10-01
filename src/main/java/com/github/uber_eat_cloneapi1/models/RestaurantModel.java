package com.github.uber_eat_cloneapi1.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="restaurants")
@Entity
public class RestaurantModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cuisine;
    private double rating;
    private String address;
    private boolean isOpen;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menus_id",referencedColumnName = "id")
    private List<MenuModel> menus = new ArrayList<>();
}
