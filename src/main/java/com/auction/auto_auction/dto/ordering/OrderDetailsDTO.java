package com.auction.auto_auction.dto.ordering;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.utils.view.ReceiptViews;
import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailsDTO {

    @JsonView({ReceiptViews.ReceiptDetails.class, Views.Public.class})
    private int id;

    @JsonView(ReceiptViews.ReceiptDetails.class)
    private double auctionRate;

    @JsonView(ReceiptViews.ReceiptDetails.class)
    private BigDecimal price;

    @JsonView(ReceiptViews.ReceiptDetails.class)
    private String orderStatus;

    @JsonView(ReceiptViews.ReceiptDetails.class)
    private LotDTO lot;
}
