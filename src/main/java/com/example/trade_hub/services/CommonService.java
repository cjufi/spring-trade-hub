package com.example.trade_hub.services;


import com.example.trade_hub.entities.AdvertisementCategory;
import com.example.trade_hub.repositories.AdvertisementCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final AdvertisementCategoryRepository advertisementCategoryRepository;


    public List<AdvertisementCategory> getAllCategories() {
      return advertisementCategoryRepository.findAll();
    }
}
