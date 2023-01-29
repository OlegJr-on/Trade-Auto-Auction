package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    Optional<List<Customer>> findAllByBidsNotNull();

    @Query("SELECT c , SUM(od.totalPrice) as spendMoney FROM Customer c " +
            "    JOIN Bid b ON c.id = b.customer.id " +
            "        JOIN Order o ON b.id = o.bid.id " +
            "            JOIN OrdersDetails od ON o.id = od.order.id " +
            "GROUP BY c.id " +
            "ORDER BY spendMoney desc")
    Optional<List<Customer>> findCustomersWhoMostSpend();
}
