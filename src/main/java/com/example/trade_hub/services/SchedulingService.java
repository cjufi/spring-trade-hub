package com.example.trade_hub.services;

import com.example.trade_hub.repositories.AdvertisementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final AdvertisementRepository advertisementRepository;

    @Scheduled(cron = "@daily")
    public void updateAdvertisementsStatus(){
        advertisementRepository.updateStatuses();
    }

    @Scheduled(cron = "@daily")
    public void updateAdvertisementsPromotions(){
        advertisementRepository.updateAdvertisementsPromotions();
    }
}
