package com.example.trade_hub.dto.advertisements;

import com.example.trade_hub.dto.users.UserResponseDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementResponseDTO {
    private Integer id;

    private String title;

    private String description;

    private byte[] picture;

    private double price;

    @JsonFormat(pattern="dd-MM-yyyy")
    private Date creationDate;

    private UserResponseDTO user;

    private String advertisementCategory;

    private String advertisementStatus;

    private String advertisementPromotion;
}
