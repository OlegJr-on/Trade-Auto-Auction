package com.auction.auto_auction.entity.enums;

public enum LotStatus {

    NOT_ACTIVE("Not active"),
    SOLD_OUT("Sold out"),
    TRADING("Trading"),
    OVERDUE("Overdue");

    public final String label;

    private LotStatus(String label) {
        this.label = label;
    }
}
