package com.auction.auto_auction.dto.statistic;

import com.auction.auto_auction.dto.CustomerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerStatisticDTO {
    private CustomerDTO customer;
    private long bidQuantity;
    private BigDecimal spendMoney;
    private long quantityWinLot;
    private double averageGrowthIndicatorByLaunchPrice;
}
