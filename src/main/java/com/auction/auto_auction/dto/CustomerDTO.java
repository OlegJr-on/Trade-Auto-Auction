package com.auction.auto_auction.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    private int id;

    @NotNull(message = "First name must be not null")
    @Size(min = 3,max = 20,message = "First name must have at least 3 and no more than 20 characters" )
    @Pattern(regexp = "^[a-zA-Z]*$",message = "First name must have only letters")
    private String firstName;

    @NotNull(message = "Last name must be not null")
    @Size(min = 3,max = 30,message = "Last name must have at least 3 and no more than 30 characters" )
    @Pattern(regexp = "^[a-zA-Z]*$",message = "Last name must have only letters")
    private String lastName;

    @NotNull(message = "The date of birth is required")
    private Date birthDay;

    @NotNull(message = "The phone number is required")
    @Pattern(regexp = "^(\\d{3}[- .]?){2}\\d{4}$",message = "Phone number should be like 0447771111")
    private String phoneNumber;

    @NotNull(message = "The location is required")
    private String location;

    @NotNull(message = "Email is required")
    @Pattern(regexp = "^(.+)@(\\S+)$",message = "Email should be like username@domain.com")
    private String email;

    @NotNull(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must have contains minimum eight characters, " +
                    "at least one letter, one number and one special character")
    private String password;

    @Builder.Default
    private List<String> roles = List.of("User");

    @Min(value = 0)
    @Max(value = 100)
    @Builder.Default
    private int discount = 0;

    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
}
