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

    public static CustomerDTO mapToCustomerDTO(User entity) {
        return CustomerDTO
                .builder()
                .id(entity.getCustomer().getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .birthDay(entity.getBirthDay())
                .email(entity.getEmail())
                .location(entity.getLocation())
                .phoneNumber(entity.getPhoneNumber())
                .discount(entity.getCustomer().getDiscount())
                .balance(entity.getCustomer().getBankAccount().getBalance())
                .roles(entity.getRoles()
                        .stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList()))
                .build();
    }

}
