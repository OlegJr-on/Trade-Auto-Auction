package com.auction.auto_auction.entity.enums;

public enum AutoState {

    RUN_AND_DRIVE("Run and drive"),
    NON_OPERATIONAL("Non Operational"),
    USED("Used");

    public final String label;

    private AutoState(String label) {
        this.label = label;
    }

    public static AutoState transform(String label){
        return AutoState.valueOf(
                label.replaceAll(" ", "_").toUpperCase());
    }
}
