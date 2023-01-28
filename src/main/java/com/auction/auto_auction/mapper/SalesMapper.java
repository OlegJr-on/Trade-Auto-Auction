package com.auction.auto_auction.mapper;

import com.auction.auto_auction.dto.SalesDepartmentDTO;
import com.auction.auto_auction.entity.SalesDepartment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = LotMapper.class)
public interface SalesMapper {

    SalesMapper INSTANCE = Mappers.getMapper(SalesMapper.class);

    SalesDepartmentDTO mapToDTO(SalesDepartment entity);

    @Mapping(target = "lots", expression = "java(null)")
    SalesDepartment mapToEntity(SalesDepartmentDTO dto);
}
