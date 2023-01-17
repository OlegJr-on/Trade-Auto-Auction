package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot,Integer> {
}
