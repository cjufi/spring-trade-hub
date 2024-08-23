package com.example.trade_hub.controllers;


import com.example.trade_hub.entities.AdvertisementCategory;
import com.example.trade_hub.services.CommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/common")
@RequiredArgsConstructor
public class CommonController {

    private final CommonService commonService;

    @GetMapping("/categories")
    public ResponseEntity<List<AdvertisementCategory>> getAllAdvertisementCategories(){
       return ResponseEntity.ok(commonService.getAllCategories());
    }

}
