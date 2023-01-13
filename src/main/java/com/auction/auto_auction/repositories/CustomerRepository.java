package com.auction.auto_auction.repositories;

import com.auction.auto_auction.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
}
