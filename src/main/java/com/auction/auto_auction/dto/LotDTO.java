package com.auction.auto_auction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotDTO {

    private int id;

    @NotNull(message = "State of lot cannot be null")
    private String lotStatus;

    @NotNull(message = "Launch price of lot cannot be null")
    @Positive(message = "Launch price of lot cannot be less than 0$")
    private BigDecimal launchPrice;

    @NotNull(message = "Minimum rate of lot cannot be null")
    @Positive(message = "Minimum rate of lot cannot be less than 0$")
    private BigDecimal minRate;

    @NotNull(message = "Start trading of lot cannot be null")
    @Future(message = "Start trading of lot should be later than just now")
    private Date startTrading;

    @NotNull(message = "The end trading of lot cannot be null")
    @Future(message = "The end trading of lot should be later than just now")
    private Date endTrading;

}
