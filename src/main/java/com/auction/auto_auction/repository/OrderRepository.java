package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
}
