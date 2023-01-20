package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car,Integer> {

    Optional<List<Car>> findByMarkAndModel(String mark,String model);
}
