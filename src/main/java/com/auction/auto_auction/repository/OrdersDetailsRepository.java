package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.OrdersDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersDetailsRepository extends JpaRepository<OrdersDetails,Integer> {
}
