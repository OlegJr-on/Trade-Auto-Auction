package com.auction.auto_auction.entity.statistic;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Document(collection = "cars_statistic_by_date")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarStatistic {

    @Id
    private String id;

    @Field("date")
    private LocalDate date;

    @Field("top10_highest_bid")
    private Map<String, BigDecimal> top10HighestBid;

    @Field("top10_most_bid_activity")
    private Map<String,Long> top10MostBidActivity;
}
