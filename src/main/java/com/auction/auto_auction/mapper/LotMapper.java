package com.auction.auto_auction.mapper;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.enums.LotStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {LotStatus.class}, uses = CarMapper.class)
public interface LotMapper {

    @Mapping(source = "entity.lotStatus.label", target = "lotStatus")
    @Mapping(target = "bids", expression = "java(null)")
    @Mapping(target = "sales", expression = "java(null)")
    @Mapping(target = "car", expression = "java(carMapper.mapToDTO(entity.getCar()))")
    LotDTO mapToDTO(Lot entity);

    @Mapping(target = "lotStatus", expression = "java(LotStatus.transform(dto.getLotStatus()))")
    @Mapping(target = "bids", expression = "java(null)")
    @Mapping(target = "salesInfo", expression = "java(null)")
    Lot mapToEntity(LotDTO dto);
}
