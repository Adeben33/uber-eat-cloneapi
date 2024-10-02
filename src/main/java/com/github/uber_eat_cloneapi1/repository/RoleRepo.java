package com.github.uber_eat_cloneapi1.repository;

import com.github.uber_eat_cloneapi1.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<RoleModel, Integer> {
    Optional<RoleModel> findByName(String name);
}
