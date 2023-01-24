package com.auction.auto_auction.dto;

import com.auction.auto_auction.utils.view.SalesViews;
import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalesDepartmentDTO {

    @JsonView(SalesViews.Public.class)
    private int id;

    @JsonView(SalesViews.Public.class)
    @NotNull(message = "Name of sales cannot be null")
    @NotBlank(message = "Name of sales cannot be empty")
    private String salesName;

    @JsonView(SalesViews.Public.class)
    @NotNull(message = "The location of sales is required")
    @NotBlank(message = "The location of sales cannot be empty")
    private String location;

    @JsonView(SalesViews.Public.class)
    @NotNull(message = "Date of sales cannot be null")
    @Future(message = "Date of sales should be later than just now")
    private LocalDateTime salesDate;

    @JsonView(SalesViews.WithTimeLeft.class)
    private String timeLeft;

    @JsonView(SalesViews.LotsDetails.class)
    private List<LotDTO> lots;
}
