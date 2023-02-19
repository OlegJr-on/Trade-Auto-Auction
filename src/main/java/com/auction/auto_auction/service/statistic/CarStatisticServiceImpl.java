package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.statistic.CarStatisticDTO;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class CarStatisticServiceImpl implements CarStatisticService{
    private UnitOfWork unitOfWork;

    @Override
    public CarStatisticDTO getBestSellingCars() {
        return null;
    }

    @Override
    public CarStatisticDTO getTop10MostSellingMarkOfCar() {
        return null;
    }

    @Override
    public CarStatisticDTO getTop10MarkBringMostIncome() {
        return null;
    }
}
