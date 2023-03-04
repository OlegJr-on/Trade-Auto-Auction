package com.auction.auto_auction.repository.statistic;

import com.auction.auto_auction.entity.statistic.CarStatistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarStatisticRepository extends MongoRepository<CarStatistic,String> {
}
