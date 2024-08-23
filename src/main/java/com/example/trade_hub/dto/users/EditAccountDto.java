package com.example.trade_hub.dto.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAccountDto {

    private String name;
    private String city;
    private String phone;

}
