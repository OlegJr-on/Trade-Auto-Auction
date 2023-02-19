package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.statistic.CarStatisticDTO;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.enums.OrderStatus;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CarStatisticServiceImpl implements CarStatisticService{
    private UnitOfWork unitOfWork;

    @Override
    public CarStatisticDTO getBestSellingCars() {

        Map<String,Long> carMarkAndCountSale = this.mapCarMarksToCount(this.getPaidCars());

        return CarStatisticDTO.builder()
                .mostSellingMark(this.reverseSortMapByValue(carMarkAndCountSale))
                .build();
    }

    @Override
    public CarStatisticDTO getTop10MostSellingMarkOfCar() {

        Map<String,Long> carMarkAndCountSale =  this.mapCarMarksToCount(this.getPaidCars());

        return CarStatisticDTO.builder()
                .mostSellingMark(this.getTop10EntriesSortedByLongValue(carMarkAndCountSale))
                .build();
    }

    @Override
    public CarStatisticDTO getTop10MarkBringMostIncome() {
        return null;
    }

    private List<Car> getPaidCars(){
        return this.unitOfWork.getCarRepository()
                .findCarByOrderStatus(OrderStatus.PAID)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found sold out cars."));
    }

    private Map<String,Long> mapCarMarksToCount(List<Car> cars){
        return cars.stream()
                .collect(Collectors.groupingBy(Car::getMark, Collectors.counting()));
    }

    private Map<String,Long> reverseSortMapByValue(Map<String, Long> map){
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
    }

    private Map<String,Long> getTop10EntriesSortedByLongValue(Map<String, Long> map){
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
    }
}
