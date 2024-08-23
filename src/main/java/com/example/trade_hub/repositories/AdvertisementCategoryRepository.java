package com.example.trade_hub.repositories;

import com.example.trade_hub.entities.AdvertisementCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementCategoryRepository extends JpaRepository<AdvertisementCategory,Integer> {

    Optional<AdvertisementCategory> findById(int id);
}
