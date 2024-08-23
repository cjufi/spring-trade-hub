package com.example.trade_hub.dto.mappers;

import com.example.trade_hub.dto.advertisements.AdvertisementResponseDTO;
import com.example.trade_hub.dto.users.UserResponseDTO;
import com.example.trade_hub.entities.Advertisement;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AdvertisementResponseMapper implements Function<Advertisement, AdvertisementResponseDTO> {
    @Override
    public AdvertisementResponseDTO apply(Advertisement ad) {
      return AdvertisementResponseDTO.builder()
                .id(ad.getId())
                .title(ad.getTitle())
                .description(ad.getDescription())
                .picture(ad.getPicture())
                .price(ad.getPrice())
                .creationDate(ad.getCreationDate())
                .user(UserResponseDTO.builder()
                        .id(ad.getAppUser().getId())
                        .email(ad.getAppUser().getEmail())
                        .name(ad.getAppUser().getName())
                        .city(ad.getAppUser().getCity())
                        .phone(ad.getAppUser().getPhone())
                        .build())
                .advertisementCategory(ad.getAdvertisementCategory().getCategoryName())
                .advertisementStatus(ad.getAdvertisementStatus().getStatusName())
                .advertisementPromotion(ad.getAdvertisementPromotion().getTitle())
                .build();
    }
}
