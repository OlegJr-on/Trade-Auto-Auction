package com.auction.auto_auction.repositories;

import com.auction.auto_auction.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid,Integer> {
    Optional<Bid> findByLotId(int lotId);
}
