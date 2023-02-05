package com.auction.auto_auction.dto.ordering;

import com.auction.auto_auction.dto.LotDTO;
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
    private int id;
    private int auctionRate;
    private BigDecimal price;
    private String orderStatus;
    private @NotNull LotDTO lot;
}
