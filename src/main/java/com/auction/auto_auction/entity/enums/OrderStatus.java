package com.auction.auto_auction.entity.enums;

public enum OrderStatus {

    PAID("Paid"),
    NOT_PAID("Not paid"),
    CANCELED("Canceled");

    public final String label;

    private OrderStatus(String label) {
        this.label = label;
    }

    public static LotStatus transform(String label){
        return LotStatus.valueOf(
                label.replaceAll(" ", "_").toUpperCase());
    }
}
