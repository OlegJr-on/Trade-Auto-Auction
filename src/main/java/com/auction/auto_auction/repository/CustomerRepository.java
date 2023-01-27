package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {

    Optional<List<Customer>> findAllByBidsNotNull();
}
