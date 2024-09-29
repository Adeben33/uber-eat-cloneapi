package com.github.uber_eat_cloneapi1.repository;

import com.github.uber_eat_cloneapi1.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleModel, Integer> {
    Optional<RoleModel> findByName(String name);
}
