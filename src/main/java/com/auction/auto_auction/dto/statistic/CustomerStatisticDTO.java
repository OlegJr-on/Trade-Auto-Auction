package com.auction.auto_auction.dto.statistic;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.utils.view.CustomerStatisticViews;
import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
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

    @JsonView(Views.Public.class)
    private CustomerDTO customer;

    @JsonView(CustomerStatisticViews.MostActivityDetails.class)
    private long bidQuantity;

    @JsonView(CustomerStatisticViews.MostSpendDetails.class)
    private BigDecimal spendMoney;

    @JsonView(CustomerStatisticViews.MostSpendDetails.class)
    private long quantityWinLot;

    @JsonView(CustomerStatisticViews.AverageGrowthIndicatorForLaunchPrice.class)
    private double averageGrowthIndicatorByLaunchPrice;

    @JsonView(CustomerStatisticViews.AverageIndicatorClosenessToNominalPrice.class)
    private double avgIndicatorClosenessToNominalPrice;
}
