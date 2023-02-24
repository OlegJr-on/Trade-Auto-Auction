package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.statistic.CarStatisticDTO;


public interface CarStatisticService {

    CarStatisticDTO getBestSellingCars();

    CarStatisticDTO getTop10MostSellingMarkOfCar();

    CarStatisticDTO getTop10MarkBringMostIncome();

    CarStatisticDTO getTop10MostPopularMarksOfCarByBidActivity();
}
