package com.github.uber_eat_cloneapi1.repository;

import com.github.uber_eat_cloneapi1.models.RestaurantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepo extends JpaRepository<RestaurantModel,Long> {
}
