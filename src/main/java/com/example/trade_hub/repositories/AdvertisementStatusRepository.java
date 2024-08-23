package com.example.trade_hub.repositories;

import com.example.trade_hub.entities.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvertisementStatusRepository extends JpaRepository<AdvertisementStatus, Integer> {
}
