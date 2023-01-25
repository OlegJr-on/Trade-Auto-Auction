package com.auction.auto_auction.dto;

import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarDTO {

    @JsonView(Views.Public.class)
    private int id;

    @JsonView(Views.Public.class)
    @NotNull(message = "Mark of car must be not null")
    @Size(min = 2,max = 20,message = "Mark of car must have at least 2 and no more than 20 characters" )
    @Pattern(regexp = "^[a-zA-Z]*$",message = "Mark of car must have only letters")
    private String mark;

    @JsonView(Views.Public.class)
    @NotNull(message = "Model of car must be not null")
    @Size(min = 2,max = 30,message = "Model of car must have at least 2 and no more than 30 characters" )
    @Pattern(regexp = "^[A-Za-z0-9]*$",message = "Model of car can have only letters and numbers")
    private String model;

    @JsonView(Views.Public.class)
    @NotNull(message = "First registry date of car cannot be null")
    @Past(message = "First registry date of car should be earlier than nowadays")
    private LocalDate registryDate;

    @JsonView(Views.Public.class)
    @NotNull(message = "Run of car cannot be null")
    @PositiveOrZero(message = "Run of car cannot be less or equal than 0")
    private int run;

    @Positive(message = "Weight of car cannot be less than 0 kg")
    private int weight;

    @NotNull(message = "Damage description cannot be null")
    @NotBlank(message = "Damage description cannot be empty")
    private String damage;

    @NotNull(message = "State of car cannot be null")
    private String autoState;

    @NotNull(message = "Nominal value of car cannot be null")
    @Positive(message = "Nominal value of car cannot be less than 0$")
    private BigDecimal nominalValue;

    private BigDecimal orientedPrice;

    private List<@NotBlank String> photosSrc = new ArrayList<>();

    @JsonIgnore
    private LotDTO lot;
}