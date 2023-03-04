package com.auction.auto_auction.job.statistic;

import com.auction.auto_auction.entity.statistic.CarStatistic;
import com.auction.auto_auction.repository.statistic.CarStatisticRepository;
import com.auction.auto_auction.service.statistic.CarStatisticService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Component
@AllArgsConstructor
public class CalculationCarStatisticsByLastDay {
    private final CarStatisticRepository carStatisticRepo;
    private final CarStatisticService carStatisticService;

    @Scheduled(cron = "${interval-statistical-data-cron}") // Every day at 3 a.m
    @Transactional
    public void calculateCarStatisticalByLastDayAndEnteredIntoDb(){

        Map<String,Long> statByBidActivity = this.getCarsStatByBidActivityLast24Hours();
        Map<String,BigDecimal> statByHighestBid = this.getCarsStatByHighestBidLast24Hours();

        CarStatistic statisticData = CarStatistic.builder()
                .date(LocalDateTime.now())
                .top10MostBidActivity(statByBidActivity)
                .top10HighestBid(statByHighestBid)
                .build();

        this.carStatisticRepo.save(statisticData);
    }

    private Map<String,Long> getCarsStatByBidActivityLast24Hours(){
        return this.carStatisticService.getTop10CarMarksByBidActivityLast24Hours()
                .getMostPopularCarMarksByBidActivity();
    }

    private Map<String, BigDecimal> getCarsStatByHighestBidLast24Hours(){
        return this.carStatisticService.getTop10CarMarksByHighestBidLast24Hours()
                .getMostProfitableCarMarks();
    }
}
