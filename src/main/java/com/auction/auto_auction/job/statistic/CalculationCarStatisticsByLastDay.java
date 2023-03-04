package com.auction.auto_auction.job.statistic;

import com.auction.auto_auction.repository.statistic.CarStatisticRepository;
import com.auction.auto_auction.service.statistic.CarStatisticService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CalculationCarStatisticsByLastDay {
    private final CarStatisticRepository carStatisticRepo;
    private final CarStatisticService carStatisticService;

}
