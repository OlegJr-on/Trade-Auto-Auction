package com.auction.auto_auction.mapper;

import com.auction.auto_auction.dto.CarDTO;
import com.auction.auto_auction.entity.AutoPhoto;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.enums.AutoState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {AutoPhoto.class, AutoState.class})
public interface CarMapper {

    @Mapping(source = "state.label", target = "autoState")
    @Mapping(target = "lot", expression = "java(null)")
    @Mapping(target = "photosSrc",
             expression = "java( entity.getPhotos().stream()" +
                                                  ".map(AutoPhoto::getPhotoSrc)" +
                                                  ".toList())")
    CarDTO mapToDTO(Car entity);

    @Mapping(target = "state", expression = "java(AutoState.transform(dto.getAutoState()))")
    @Mapping(target = "lot", expression = "java(null)")
    @Mapping(target = "photos", expression = "java(dto.getPhotosSrc().stream()" +
                                                                    ".map(AutoPhoto::new)" +
                                                                    ".toList())")
    Car mapToEntity(CarDTO dto);
}
