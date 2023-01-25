package com.auction.auto_auction.dto;

import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LotDTO {

    @JsonView(Views.Public.class)
    private int id;

    @JsonView(Views.Public.class)
    private String lotStatus;

    @JsonView(Views.Public.class)
    @NotNull(message = "Launch price of lot cannot be null")
    @Positive(message = "Launch price of lot cannot be less than 0$")
    private BigDecimal launchPrice;

    @JsonView(Views.Public.class)
    @NotNull(message = "Minimum rate of lot cannot be null")
    @Positive(message = "Minimum rate of lot cannot be less than 0$")
    private BigDecimal minRate;

    @JsonView(Views.Public.class)
    @NotNull(message = "Start trading of lot cannot be null")
    @Future(message = "Start trading of lot should be later than just now")
    private LocalDateTime startTrading;

    @JsonView(Views.Public.class)
    @NotNull(message = "The end trading of lot cannot be null")
    @Future(message = "The end trading of lot should be later than just now")
    private LocalDateTime endTrading;

    private @Valid CarDTO car;

    @JsonIgnore
    private List<SalesDepartmentDTO> sales;
}
