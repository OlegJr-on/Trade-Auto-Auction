package com.auction.auto_auction.dto.ordering;

import com.auction.auto_auction.utils.view.ReceiptViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReceiptDTO {

    @JsonView(ReceiptViews.ReceiptDetails.class)
    private List<OrderDetailsDTO> orders;

    @JsonView(ReceiptViews.ReceiptDetails.class)
    private BigDecimal total;
}
