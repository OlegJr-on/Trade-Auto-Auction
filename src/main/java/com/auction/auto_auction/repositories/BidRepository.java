package com.auction.auto_auction.repositories;

import com.auction.auto_auction.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidRepository extends JpaRepository<Bid,Integer> {
}
