package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.CarDTO;
import com.auction.auto_auction.entity.*;
import com.auction.auto_auction.entity.enums.AutoState;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.CarService;
import com.auction.auto_auction.utils.ApplicationMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private final UnitOfWork unitOfWork;

    public CarServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public List<CarDTO> findAll() {

        List<Car> carsFromSource = this.unitOfWork.getCarRepository().findAll();

        if (carsFromSource.isEmpty()){
            throw new ResourceNotFoundException("Data source is empty");
        }

        return carsFromSource.stream()
                             .map(ApplicationMapper::mapToCarDTO)
                             .toList();
    }

    @Override
    public CarDTO findById(int carId) {

        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        return ApplicationMapper.mapToCarDTO(carEntity);
    }

    @Override
    public List<CarDTO> findByMark(String mark) {

        Optional<List<Car>> carEntities = this.unitOfWork.getCarRepository().findByMark(mark);

        if (carEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Car","mark",mark);
        }

        return carEntities.get().stream()
                                .map(ApplicationMapper::mapToCarDTO)
                                .toList();
    }

    @Override
    public List<CarDTO> findByMarkAndModel(String mark, String model) {

        Optional<List<Car>> carEntities = this.unitOfWork.getCarRepository().findByMarkAndModel(mark,model);

        if (carEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Car","mark and model", String.format("%s %s",mark,model));
        }

        return carEntities.get().stream()
                                .map(ApplicationMapper::mapToCarDTO)
                                .toList();
    }

    @Override
    public void create(CarDTO createdCar) {

        if (createdCar == null) {
            throw new NullPointerException("Car doesn`t created, values is null");
        }

        // map dto to entity
        Car carEntity = ApplicationMapper.mapToCarEntity(createdCar);

        // create relevant photos entities from given dto
        List<AutoPhoto> photos = createdCar.getPhotosSrc().stream()
                                                          .map(AutoPhoto::new)
                                                          .toList();

        // set photos into car entity and in setter act two-ways relation
        carEntity.setPhotos(photos);

        // save new data into data source
        this.unitOfWork.getCarRepository().save(carEntity);

    }

    @Override
    public void update(int carId, CarDTO updatedCar) {

        if (updatedCar == null) {
            throw new NullPointerException("Car doesn`t updated, entered values is null");
        }

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
        carEntity.setState(AutoState.valueOf(updatedCar.getAutoState()
                                                           .replaceAll(" ", "_")
                                                           .toUpperCase()));
        carEntity.setNominalValue(updatedCar.getNominalValue());

        if (updatedCar.getOrientedPrice() != null){
            carEntity.setOrientedPrice(updatedCar.getOrientedPrice());
        }

        // save changes
        this.unitOfWork.getCarRepository().save(carEntity);
    }

    @Override
    public void deleteById(int carId) {

    }
}
