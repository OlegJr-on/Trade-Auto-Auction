package com.auction.auto_auction.mapper;

import com.auction.auto_auction.dto.BidDTO;
import com.auction.auto_auction.entity.Bid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class,LotMapper.class})
public interface BidMapper {

    BidMapper INSTANCE = Mappers.getMapper(BidMapper.class);

    @Mapping(source = "entity.active", target = "win")
    BidDTO mapToDTO(Bid entity);
}
