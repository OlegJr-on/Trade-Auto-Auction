package com.auction.auto_auction.mapper;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.Role;
import com.auction.auto_auction.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {Role.class, Collectors.class})
public interface CustomerMapper {

    @Mapping(source = "entity.user.firstName", target = "firstName")
    @Mapping(source = "entity.user.lastName", target = "lastName")
    @Mapping(source = "entity.user.birthDay", target = "birthDay")
    @Mapping(source = "entity.user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "entity.user.location", target = "location")
    @Mapping(source = "entity.user.email", target = "email")
    @Mapping(source = "entity.bankAccount.balance", target = "balance")
    @Mapping(target = "bids", expression = "java(null)")
    @Mapping(target = "roles",
             expression = "java(entity.getUser().getRoles().stream()" +
                                                          ".map(Role::getRoleName)" +
                                                          ".collect(Collectors.toList()))")
    CustomerDTO mapToDTO(Customer entity);

    @Mapping(source = "entity.customer.id", target = "id")
    @Mapping(target = "password", expression = "java(null)")
    @Mapping(source = "entity.customer.discount", target = "discount")
    @Mapping(source = "entity.customer.bankAccount.balance", target = "balance")
    @Mapping(target = "roles", expression = "java(entity.getRoles().stream()" +
                                                                  ".map(Role::getRoleName)" +
                                                                  ".collect(Collectors.toList()))")
    CustomerDTO mapToDTO(User entity);

    @Mapping(target = "roles", expression = "java(null)")
    User mapToEntity(CustomerDTO dto);
}
