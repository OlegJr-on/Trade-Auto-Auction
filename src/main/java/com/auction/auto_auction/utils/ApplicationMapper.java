package com.auction.auto_auction.utils;

import com.auction.auto_auction.dto.CarDTO;
import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.entity.*;

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

    public static User mapToUserEntity(CustomerDTO dto) {
        return User
                .builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .location(dto.getLocation())
                .birthDay(dto.getBirthDay())
                .build();
    }

    public static CarDTO mapToCarDTO(Car entity) {
        return CarDTO
                .builder()
                .id(entity.getId())
                .mark(entity.getMark())
                .model(entity.getModel())
                .registryDate(entity.getRegistryDate())
                .run(entity.getRun())
                .weight(entity.getWeight())
                .damage(entity.getDamage())
                .autoState(entity.getState().label)
                .nominalValue(entity.getNominalValue())
                .orientedPrice(entity.getOrientedPrice())
                .photosSrc(entity.getPhotos()
                                    .stream()
                                    .map(AutoPhoto::getPhotoSrc)
                                    .toList())
                .build();
    }
}
