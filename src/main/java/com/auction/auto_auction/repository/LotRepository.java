package com.auction.auto_auction.repository;

import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.enums.LotStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LotRepository extends JpaRepository<Lot,Integer> {

    Optional<List<Lot>> findByIdIn(Collection<Integer> ids);

    Optional<List<Lot>> findByStartTradingBetween(LocalDateTime start, LocalDateTime end);

    Optional<List<Lot>> findByStartTradingBefore(LocalDateTime date);

    Optional<List<Lot>> findByStartTradingAfter(LocalDateTime date);

    Optional<List<Lot>> findByLotStatusNotIn(Set<LotStatus> exceptedStatuses);
}

