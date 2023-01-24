package com.auction.auto_auction.dto;

import com.auction.auto_auction.utils.view.CustomerViews;
import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    @JsonView(Views.Public.class)
    private int id;

    @JsonView({CustomerViews.UserDetails.class,CustomerViews.CustomerDetails.class})
    @NotNull(message = "First name must be not null")
    @Size(min = 3,max = 20,message = "First name must have at least 3 and no more than 20 characters" )
    @Pattern(regexp = "^[a-zA-Z]*$",message = "First name must have only letters")
    private String firstName;

    @JsonView({CustomerViews.UserDetails.class,CustomerViews.CustomerDetails.class})
    @NotNull(message = "Last name must be not null")
    @Size(min = 3,max = 30,message = "Last name must have at least 3 and no more than 30 characters" )
    @Pattern(regexp = "^[a-zA-Z]*$",message = "Last name must have only letters")
    private String lastName;

    @JsonView(CustomerViews.UserDetails.class)
    @NotNull(message = "The date of birth is required")
    @Past(message = "Date of birthday should be in the past")
    private LocalDate birthDay;

    @JsonView(CustomerViews.UserDetails.class)
    @NotNull(message = "The phone number is required")
    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{4}$",message = "Phone number should be like 0447771111")
    private String phoneNumber;

    @JsonView(CustomerViews.UserDetails.class)
    @NotNull(message = "The location is required")
    private String location;

    @JsonView({CustomerViews.UserDetails.class,CustomerViews.CustomerDetails.class})
    @NotNull(message = "Email is required")
    @Pattern(regexp = "^(.+)@(\\S+)$",message = "Email should be like username@domain.com")
    private String email;

    @JsonView(CustomerViews.PasswordDetails.class)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must have contains minimum eight characters, " +
                    "at least one letter, one number and one special character")
    private String password;

    @JsonView(CustomerViews.UserDetails.class)
    @Builder.Default
    private List<String> roles = List.of("User");

    @JsonView(CustomerViews.CustomerDetails.class)
    @PositiveOrZero
    @Max(value = 100)
    @Builder.Default
    private int discount = 0;

    @JsonView(CustomerViews.CustomerDetails.class)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
}
