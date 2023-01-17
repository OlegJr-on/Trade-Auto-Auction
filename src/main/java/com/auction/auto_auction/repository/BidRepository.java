package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid,Integer> {

    Optional<List<Bid>> findByLotId(int lotId);

    Optional<List<Customer>> findByCustomerId(int customerId);
}
