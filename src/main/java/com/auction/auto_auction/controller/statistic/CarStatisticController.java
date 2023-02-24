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

    @GetMapping("/top-10/most-income")
    public ResponseEntity<Map<String, BigDecimal>> getTop10CarBrandsBringMostIncome(){

        CarStatisticDTO brandToIncome = this.carStatisticService.getTop10MarkBringMostIncome();

        return ResponseEntity.ok(brandToIncome.getMostProfitableCarMarks());
    }

    @GetMapping("/top-10/most-selling")
    public ResponseEntity<Map<String,Long>> getTop10MostSellingCarBrands(){

        CarStatisticDTO carBrandToQuantitySale = this.carStatisticService.getTop10MostSellingMarkOfCar();

        return ResponseEntity.ok(carBrandToQuantitySale.getMostSellingMark());
    }

    @GetMapping("/top-10/most-bid-activity")
    public ResponseEntity<Map<String,Long>> getTop10MostPopularCarMarksByBidActivity(){

        CarStatisticDTO top10CarBrands = carStatisticService.getTop10MostPopularMarksOfCarByBidActivity();

        return ResponseEntity.ok(top10CarBrands.getMostPopularCarMarksByBidActivity());
    }
}
