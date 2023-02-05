package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("SELECT o FROM Order AS o" +
            "    JOIN Bid AS b ON b.id = o.bid.id " +
            "WHERE b.customer.id = ?1")
    Optional<List<Order>> findOrdersByCustomerId(int customerId);
}
