package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car,Integer> {
}
