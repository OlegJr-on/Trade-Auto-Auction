package com.auction.auto_auction.repositories;

import com.auction.auto_auction.entities.Lot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotRepository extends JpaRepository<Lot,Integer> {
}
