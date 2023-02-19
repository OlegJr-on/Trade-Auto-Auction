package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.statistic.CarStatisticDTO;


public interface CarStatisticService {

    CarStatisticDTO getTop10MostSellingMarkOfCar();
}
