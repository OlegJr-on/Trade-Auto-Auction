package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer> {

    Optional<List<Car>> findByMark(String mark);

    Optional<List<Car>> findByMarkAndModel(String mark,String model);

    @Query("SELECT car FROM OrdersDetails AS od " +
            "JOIN Order o on o.id = od.order.id " +
            "JOIN Bid b on b.id = o.bid.id " +
            "JOIN Lot l on l.id = b.lot.id " +
            "JOIN Car car on car.id = l.car.id " +
            "WHERE od.orderStatus =  ?1")
    Optional<List<Car>> findCarByOrderStatus(OrderStatus orderStatus);

    @Query("SELECT car.mark, COUNT(b) AS bidCount FROM Bid AS b " +
            " JOIN Lot AS l on l.id = b.lot.id " +
            " JOIN Car AS car on car.id = l.car.id " +
            "GROUP BY car.mark " +
            "ORDER BY bidCount desc")
    Optional<List<Object[]>> findCarsOrderedByBidActivity();
}
