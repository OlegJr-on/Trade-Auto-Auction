package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.statistic.CarStatisticDTO;
import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.OrdersDetails;
import com.auction.auto_auction.entity.enums.OrderStatus;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.projection.CarBidActivity;
import com.auction.auto_auction.repository.projection.CarMarkToBid;
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

    @Override
    public CarStatisticDTO getTop10CarsMarkByCommissionIncome() {

        Map<String, BigDecimal> carBrandToCommissionIncome =
                this.mapCarMarksToCommissionIncome(this.getPaidCars());

        return CarStatisticDTO.builder()
                .highestEarningCarMarksByCommission(this.getTop10EntriesSortedByValue(carBrandToCommissionIncome))
                .build();
    }

    @Override
    public CarStatisticDTO getCarMarkBidActivityLast24Hours() {

        List<CarBidActivity> carsByBidActivityFor24Hours = this.unitOfWork.getCarRepository()
                .findCarMarksOrderedByBidActivityForLast24Hours()
                    .orElseThrow(ResourceNotFoundException::new);

        Map<String,Long> carsByBidActivity = this.mapCarBidActivityList(carsByBidActivityFor24Hours);

        return CarStatisticDTO.builder()
                .mostPopularCarMarksByBidActivity(carsByBidActivity)
                .build();
    }

    @Override
    public CarStatisticDTO getTop10CarMarksByBidActivityLast24Hours() {

        List<CarBidActivity> carsByBidActivityFor24Hours = this.unitOfWork.getCarRepository()
                .findCarMarksOrderedByBidActivityForLast24Hours()
                    .orElseThrow(ResourceNotFoundException::new);

        Map<String,Long> carsByBidActivity = this.mapCarBidActivityList(carsByBidActivityFor24Hours);

        return CarStatisticDTO.builder()
                .mostPopularCarMarksByBidActivity(this.getTop10EntriesSortedByValue(carsByBidActivity))
                .build();
    }

    @Override
    public CarStatisticDTO getTop10CarMarksByHighestBidLast24Hours() {

        List<CarMarkToBid> carsWithHighestBidPast24Hour = this.unitOfWork.getCarRepository()
                .findCarMarksOrderedByHighestBidForLast24hours()
                    .orElseThrow(ResourceNotFoundException::new);

        Map<String,BigDecimal> carsToMap = this.carMarkToBidListAsMap(carsWithHighestBidPast24Hour);

        return CarStatisticDTO.builder()
                .mostProfitableCarMarks(this.getTop10EntriesSortedByValue(carsToMap))
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

    private BigDecimal getCarSaleCommissionIncome(Car car){

        Bid winBid = car.getLot().getWinBid();
        OrdersDetails order = winBid.getOrder().getLastOrderDetail();

        if (order.getOrderStatus() != OrderStatus.PAID)
            return BigDecimal.ZERO;

        BigDecimal salePrice = order.getTotalPrice();
        BigDecimal bet = winBid.getBet();
        BigDecimal saleCommission = salePrice.subtract(bet);

        return saleCommission.setScale(2,RoundingMode.CEILING);
    }

    private List<CarBidActivity> getCarMarksAndTheirBidQuantity(){
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

    private Map<String,BigDecimal> mapCarMarksToCommissionIncome(List<Car> cars){
        return cars.stream()
                .collect(Collectors.groupingBy(Car::getMark,
                        Collectors.mapping(this::getCarSaleCommissionIncome,
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

    private Map<String,Long> mapCarBidActivityList(List<CarBidActivity> listCarBid){
        return listCarBid.stream()
                .collect(Collectors.toMap(
                        CarBidActivity::getCarMark,
                        CarBidActivity::getCountBid,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
    }

    private Map<String,BigDecimal> carMarkToBidListAsMap(List<CarMarkToBid> listCarBid){
        return listCarBid.stream()
                .collect(Collectors.toMap(
                        CarMarkToBid::getCarMark,
                        CarMarkToBid::getBidValue,
                        (oldValue, newValue) -> newValue,
                        LinkedHashMap::new));
    }

    private Map<String,Long> getTop10EntriesAsMap(List<CarBidActivity> arrObjList){
        return arrObjList.stream()
                .limit(10)
                .collect(Collectors.toMap(
                        CarBidActivity::getCarMark,
                        CarBidActivity::getCountBid,
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
