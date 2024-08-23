package com.example.trade_hub.dto.advertisements;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdvertisementStatusResponseDTO {

    private Integer id;

    private String statusName;
}
