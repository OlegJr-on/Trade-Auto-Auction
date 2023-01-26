package com.auction.auto_auction.dto;

import com.auction.auto_auction.utils.view.BidViews;
import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BidDTO {

    @JsonView({Views.Public.class,BidViews.BidDetails.class})
    private int id;

    @JsonView({Views.Public.class,BidViews.BidDetails.class})
    private LocalDateTime operationDate;

    @JsonView({Views.Public.class,BidViews.BidDetails.class})
    private boolean win;

    @JsonView({Views.Public.class,BidViews.BidDetails.class})
    private BigDecimal bet;

    @JsonView({Views.Public.class, BidViews.LotDetails.class})
    private LotDTO lot;

    @JsonView({Views.Public.class, BidViews.CustomerDetails.class})
    private CustomerDTO customer;
}
