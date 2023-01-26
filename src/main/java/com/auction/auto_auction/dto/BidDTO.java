package com.auction.auto_auction.dto;

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

    private int id;

    private LocalDateTime operationDate;

    private boolean win;

    private BigDecimal bet;
}
