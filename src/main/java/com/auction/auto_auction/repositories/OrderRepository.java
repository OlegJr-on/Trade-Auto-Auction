package com.auction.auto_auction.repositories;

import com.auction.auto_auction.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
}
