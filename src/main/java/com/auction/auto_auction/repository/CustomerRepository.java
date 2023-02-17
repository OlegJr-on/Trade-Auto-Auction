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

    @Query("SELECT cus FROM Customer AS cus " +
            "JOIN BankAccount AS ba ON cus.id = ba.customer.id " +
                "WHERE ba.balance > 0")
    Optional<List<Customer>> findCustomersWhoseBalanceMoreThanZero();

    @Query("SELECT cus FROM OrdersDetails AS od " +
           "JOIN Order AS or on or.id = od.order.id "+
            "JOIN Bid AS b on b.id = or.bid.id " +
            "JOIN Customer AS cus on cus.id = b.customer.id " +
            "GROUP BY cus.id")
    Optional<List<Customer>> findCustomerWhoHaveAnyOrders();

    @Query("SELECT cus FROM OrdersDetails AS od " +
            "JOIN Order AS or on or.id = od.order.id " +
            "JOIN Bid b on b.id = or.bid.id " +
            "JOIN Customer cus on cus.id = b.customer.id " +
            "WHERE od.orderStatus = 'PAID' " +
            "GROUP BY cus.id ")
    Optional<List<Customer>> findCustomerWithPaidOrders();
}
