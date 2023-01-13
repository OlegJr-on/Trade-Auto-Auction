package com.auction.auto_auction.repositories;

import com.auction.auto_auction.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car,Integer> {
}
