package com.auction.auto_auction.repository.projection;

import java.math.BigDecimal;

public interface CarMarkToBid {
    String getCarMark();
    BigDecimal getBidValue();
}
