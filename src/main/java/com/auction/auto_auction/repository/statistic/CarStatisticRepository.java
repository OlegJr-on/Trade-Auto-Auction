package com.auction.auto_auction.repository.statistic;

import com.auction.auto_auction.entity.statistic.CarStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CarStatisticRepository extends MongoRepository<CarStatistic,String> {

    Optional<List<CarStatistic>> findByDateAfter(LocalDateTime event);

    Optional<List<CarStatistic>> findByDateBefore(LocalDateTime event);

    Optional<List<CarStatistic>> findByDateBetween(LocalDateTime start, LocalDateTime end);
}
