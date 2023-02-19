package com.auction.auto_auction.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CarStatisticDTO {
    private Map<String,Long> mostSellingMark;
    private Map<String, BigDecimal> marksBringMostIncome;
}
