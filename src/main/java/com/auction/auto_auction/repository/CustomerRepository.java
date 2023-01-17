package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
