package com.auction.auto_auction.controller.statistic;

import com.auction.auto_auction.dto.statistic.CarStatisticDTO;
import com.auction.auto_auction.service.statistic.CarStatisticService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api-auction/statistic/cars")
@AllArgsConstructor
public class CarStatisticController {
    private final CarStatisticService carStatisticService;

    @GetMapping("/most-income")
    public ResponseEntity<Map<String, BigDecimal>> getTop10CarBrandsBringMostIncome(){

        CarStatisticDTO brandToIncome = this.carStatisticService.getTop10MarkBringMostIncome();

        return ResponseEntity.ok(brandToIncome.getMostProfitableCarMarks());
    }
}
