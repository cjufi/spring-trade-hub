package com.example.trade_hub.repositories;

import com.example.trade_hub.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {

    Optional<UserRole> findByRoleNameContaining(String name);
}
