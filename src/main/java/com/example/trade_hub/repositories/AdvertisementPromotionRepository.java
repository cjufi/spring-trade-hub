package com.example.trade_hub.repositories;

import com.example.trade_hub.entities.AdvertisementPromotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementPromotionRepository extends JpaRepository<AdvertisementPromotion,Integer> {

    Optional<AdvertisementPromotion> findById(int id);
}
