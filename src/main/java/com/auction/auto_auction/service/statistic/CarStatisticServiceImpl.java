package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.statistic.CarStatisticDTO;
import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.OrdersDetails;
import com.auction.auto_auction.entity.enums.OrderStatus;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
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
                .mostSellingMark(this.getTop10EntriesSortedByValue(carMarkAndCountSale))
                .build();
    }

    @Override
    public CarStatisticDTO getTop10MarkBringMostIncome() {

        Map<String, BigDecimal> carMarkAndHisIncome =  this.mapCarMarksToIncome(this.getPaidCars());

        return CarStatisticDTO.builder()
                .mostProfitableCarMarks(this.getTop10EntriesSortedByValue(carMarkAndHisIncome))
                .build();
    }

    @Override
    public CarStatisticDTO getTop10MostPopularMarksOfCarByBidActivity() {

        Map<String,Long> cars = this.getTop10EntriesAsMap(this.getCarMarksAndTheirBidQuantity());

        return CarStatisticDTO.builder()
                .mostPopularCarMarksByBidActivity(cars)
                .build();
    }

    private List<Car> getPaidCars(){
        return this.unitOfWork.getCarRepository()
                .findCarByOrderStatus(OrderStatus.PAID)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found sold out cars."));
    }

    private BigDecimal getSoldPriceOfCar(Car car){

        Lot carLot = car.getLot();
        Bid winBid = carLot.getWinBid();
        OrdersDetails order = winBid.getOrder().getLastOrderDetail();

        return order.getTotalPrice().setScale(2, RoundingMode.CEILING);
    }

    private List<Object[]> getCarMarksAndTheirBidQuantity(){
        return this.unitOfWork.getCarRepository()
                .findCarsOrderedByBidActivity()
                    .orElseThrow(ResourceNotFoundException::new);
    }

    private Map<String,Long> mapCarMarksToCount(List<Car> cars){
        return cars.stream()
                .collect(Collectors.groupingBy(Car::getMark, Collectors.counting()));
    }

    private Map<String,BigDecimal> mapCarMarksToIncome(List<Car> cars){
        return cars.stream()
                .collect(Collectors.groupingBy(Car::getMark,
                        Collectors.mapping(this::getSoldPriceOfCar,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
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

    private Map<String,Long> getTop10EntriesAsMap(List<Object[]> arrObjList){
        return arrObjList.stream()
                .limit(10)
                .collect(Collectors.toMap(
                    key -> String.valueOf( Arrays.stream(key).findFirst().orElse("-") ),
                    value -> (Long)List.of(value).get(value.length-1),
                    (oldValue, newValue) -> oldValue,
                    LinkedHashMap::new));
    }

    private <V extends Number>
    Map<String,V> getTop10EntriesSortedByValue(Map<String, V> map){
        return map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue((o1, o2) -> Double.compare(o2.doubleValue(),o1.doubleValue())))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
    }
}
