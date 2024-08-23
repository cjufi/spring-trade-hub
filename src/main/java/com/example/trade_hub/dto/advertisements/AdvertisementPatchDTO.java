package com.example.trade_hub.dto.advertisements;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementPatchDTO {

    private String title;

    private String description;

    private Double price;

    private Integer advertisementStatus;
}
