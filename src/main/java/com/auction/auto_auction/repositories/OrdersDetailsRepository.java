package com.auction.auto_auction.repositories;

import com.auction.auto_auction.entities.OrdersDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersDetailsRepository extends JpaRepository<OrdersDetails,Integer> {
}
