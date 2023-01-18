package com.auction.auto_auction.utils;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.Role;
import com.auction.auto_auction.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicationMapper {

    public static CustomerDTO mapToCustomerDTO(Customer entity) {
        return CustomerDTO
                .builder()
                .id(entity.getId())
                .firstName(entity.getUser().getFirstName())
                .lastName(entity.getUser().getLastName())
                .birthDay(entity.getUser().getBirthDay())
                .email(entity.getUser().getEmail())
                .location(entity.getUser().getLocation())
                .phoneNumber(entity.getUser().getPhoneNumber())
                .discount(entity.getDiscount())
                .balance(entity.getBankAccount().getBalance())
                .roles(entity.getUser().getRoles()
                        .stream().map(Role::getRoleName).collect(Collectors.toList()))
                .build();
    }

}
