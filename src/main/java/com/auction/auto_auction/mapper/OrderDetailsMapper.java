package com.auction.auto_auction.mapper;

import com.auction.auto_auction.dto.ordering.OrderDetailsDTO;
import com.auction.auto_auction.entity.OrdersDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper(componentModel = "spring", uses = {LotMapper.class})
public interface OrderDetailsMapper {

    @Mappings({
            @Mapping(target = "lot", source = "entity.order.bid.lot"),
            @Mapping(source = "entity.auctionRate", target = "auctionRate"),
            @Mapping(source = "entity.orderStatus.label", target = "orderStatus"),
            @Mapping(target = "price", expression = "java(entity.getTotalPrice().setScale(2))")
    })
    OrderDetailsDTO mapToDTO(OrdersDetails entity);
}