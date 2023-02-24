package com.auction.auto_auction.controller.statistic;

import com.auction.auto_auction.service.statistic.CarStatisticService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-auction/statistic/cars")
@AllArgsConstructor
public class CarStatisticController {
    private final CarStatisticService carStatisticService;
}
