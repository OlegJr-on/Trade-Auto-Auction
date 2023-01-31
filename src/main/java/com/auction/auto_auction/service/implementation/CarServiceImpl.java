package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.CarDTO;
import com.auction.auto_auction.entity.*;
import com.auction.auto_auction.entity.enums.AutoState;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.mapper.CarMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.CarService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarServiceImpl implements CarService {
    private final UnitOfWork unitOfWork;
    private final CarMapper carMapper;

    public CarServiceImpl(UnitOfWork unitOfWork, CarMapper carMapper) {
        this.unitOfWork = unitOfWork;
        this.carMapper = carMapper;
    }

    @Override
    public List<CarDTO> findAll() {

        List<Car> carsFromSource = this.unitOfWork.getCarRepository().findAll();

        return carsFromSource.stream()
                             .map(this.carMapper::mapToDTO)
                             .toList();
    }

    @Override
    public CarDTO findById(int carId) {

        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        return this.carMapper.mapToDTO(carEntity);
    }

    @Override
    public List<CarDTO> findByMark(String mark) {

        List<Car> carEntities = this.unitOfWork.getCarRepository()
                                               .findByMark(mark)
                                               .orElseThrow(() ->
                                                    new ResourceNotFoundException("Car","mark",mark));

        return carEntities.stream()
                          .map(this.carMapper::mapToDTO)
                          .toList();
    }

    @Override
    public List<CarDTO> findByMarkAndModel(String mark, String model) {

        List<Car> carEntities = this.unitOfWork.getCarRepository()
                .findByMarkAndModel(mark,model)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Car","mark and model", String.format("%s %s",mark,model)));

        return carEntities.stream()
                          .map(this.carMapper::mapToDTO)
                          .toList();
    }

    @Override
    public void create(@NotNull CarDTO createdCar) {

        // map dto to entity
        Car carEntity = this.carMapper.mapToEntity(createdCar);

        // set carEntity into every just created photoEntity - it`s for two-way relative
        carEntity.getPhotos().forEach(photo -> photo.setCar(carEntity));

        // save new data into data source
        this.unitOfWork.getCarRepository().save(carEntity);

    }

    @Override
    public void update(int carId, @NotNull CarDTO updatedCar) {

        // search car entity by id from data source
        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        //update info
        carEntity.setMark(updatedCar.getMark());
        carEntity.setModel(updatedCar.getModel());
        carEntity.setRegistryDate(updatedCar.getRegistryDate());
        carEntity.setRun(updatedCar.getRun());
        carEntity.setWeight(updatedCar.getWeight());
        carEntity.setDamage(updatedCar.getDamage());
        carEntity.setState(AutoState.transform(updatedCar.getAutoState()));
        carEntity.setNominalValue(updatedCar.getNominalValue());

        if (updatedCar.getOrientedPrice() != null){
            carEntity.setOrientedPrice(updatedCar.getOrientedPrice());
        }

        // save changes
        this.unitOfWork.getCarRepository().save(carEntity);
    }

    @Override
    public void deleteById(int carId) {

        // search car entity from data source by id
        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        // get related photos from car entity
        List<AutoPhoto> photoEntities = carEntity.getPhotos();

        // delete data by ids
        photoEntities.forEach(photo ->
                this.unitOfWork.getAutoPhotoRepository().deleteById(photo.getId()));

        this.unitOfWork.getCarRepository().deleteById(carEntity.getId());
    }
}
