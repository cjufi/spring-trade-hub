package com.example.trade_hub.repositories;

import com.example.trade_hub.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser,String> {


    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(int id);


}
