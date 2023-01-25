package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid,Integer> {

    Optional<List<Bid>> findByLotId(int lotId);

    Optional<List<Bid>> findByCustomerId(int customerId);

    Optional<List<Bid>> findByOperationDateBetween(LocalDateTime start, LocalDateTime end);

    Optional<List<Bid>> findByOperationDateBefore(LocalDateTime date);

    Optional<List<Bid>> findByOperationDateAfter(LocalDateTime date);
}
