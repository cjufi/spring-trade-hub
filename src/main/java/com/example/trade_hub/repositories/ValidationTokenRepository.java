package com.example.trade_hub.repositories;

import com.example.trade_hub.entities.ValidationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ValidationTokenRepository extends JpaRepository<ValidationToken,Integer> {

    Optional<ValidationToken> findByToken(String token);

}