package com.auction.auto_auction.utils;

import com.auction.auto_auction.dto.*;
import com.auction.auto_auction.entity.*;
import com.auction.auto_auction.entity.enums.AutoState;
import com.auction.auto_auction.entity.enums.LotStatus;

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

    public static LotDTO mapToLotDTO(Lot entity){
        return LotDTO
                .builder()
                .id(entity.getId())
                .lotStatus(entity.getLotStatus().label)
                .launchPrice(entity.getLaunchPrice())
                .minRate(entity.getMinRate())
                .startTrading(entity.getStartTrading())
                .endTrading(entity.getEndTrading())
                .car(ApplicationMapper.mapToCarDTO(entity.getCar()))
                .build();
    }

    public static SalesDepartmentDTO mapToSalesDepartmentDTO(SalesDepartment entity){
        return SalesDepartmentDTO
                .builder()
                .id(entity.getId())
                .location(entity.getLocation())
                .salesName(entity.getSalesName())
                .salesDate(entity.getSalesDate())
                .lots(entity.getLots().stream()
                                      .map(ApplicationMapper::mapToLotDTO)
                                      .toList())
                .build();
    }

    public static SalesDepartment mapToSalesDepartmentEntity(SalesDepartmentDTO dto){
        return SalesDepartment
                .builder()
                .salesName(dto.getSalesName())
                .salesDate(dto.getSalesDate())
                .location(dto.getLocation())
                .build();
    }

    public static BidDTO mapToBidDTO(Bid entity){
        return BidDTO
                .builder()
                .id(entity.getId())
                .operationDate(entity.getOperationDate())
                .win(entity.isActive())
                .bet(entity.getBet())
                .customer(ApplicationMapper.mapToCustomerDTO(entity.getCustomer()))
                .lot(ApplicationMapper.mapToLotDTO(entity.getLot()))
                .build();
    }
}
