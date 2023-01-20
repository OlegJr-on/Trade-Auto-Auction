package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.CarDTO;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.User;
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
    public void create(CarDTO carDTO) {

    }

    @Override
    public void update(int carId, CarDTO carDTO) {

    }

    @Override
    public void deleteById(int carId) {

    }
}
