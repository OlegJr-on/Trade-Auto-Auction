package com.auction.auto_auction.dto.ordering;

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
    private List<OrderDetailsDTO> orders;
    private BigDecimal total;
}
