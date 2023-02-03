package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.OrdersDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrdersDetailsRepository extends JpaRepository<OrdersDetails,Integer> {

    @Query("SELECT od FROM OrdersDetails od" +
           "    JOIN Order o ON o.id = od.order.id " +
           "        JOIN Bid b ON b.id = o.bid.id " +
           "            JOIN Customer c ON c.id = b.customer.id " +
           "WHERE c.id = ?1")
    Optional<List<OrdersDetails>> findAllOrdersByCustomerId(int customerId);
}
