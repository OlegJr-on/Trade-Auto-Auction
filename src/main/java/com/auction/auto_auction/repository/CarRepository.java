package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.enums.OrderStatus;
import com.auction.auto_auction.repository.projection.CarBidActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer> {

    Optional<List<Car>> findByMark(String mark);

    Optional<List<Car>> findByMarkAndModel(String mark,String model);

    Optional<List<Car>> findAllByLotBidsNotNull();

    @Query("SELECT car FROM OrdersDetails AS od " +
            "JOIN Order o on o.id = od.order.id " +
            "JOIN Bid b on b.id = o.bid.id " +
            "JOIN Lot l on l.id = b.lot.id " +
            "JOIN Car car on car.id = l.car.id " +
            "WHERE od.orderStatus =  ?1")
    Optional<List<Car>> findCarByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT car.mark AS carMark, COUNT(b) AS countBid FROM Bid AS b " +
            " JOIN Lot AS l on l.id = b.lot.id " +
            " JOIN Car AS car on car.id = l.car.id " +
            "GROUP BY carMark " +
            "ORDER BY countBid desc")
    Optional<List<CarBidActivity>> findCarsOrderedByBidActivity();

    @Query(nativeQuery = true, value =
           "SELECT car.mark AS carMark, count(b) AS countBid FROM bids AS b " +
                   "JOIN lots l ON l.id = b.lot_id " +
                   " JOIN cars car ON car.id = l.car_id " +
           "WHERE b.operation_date >= (NOW() - INTERVAL '22 hours') " +
           "GROUP BY carMark " +
           "ORDER BY countBid DESC")
    Optional<List<CarBidActivity>> findCarMarksOrderedByBidActivityForLast24Hours();
}
