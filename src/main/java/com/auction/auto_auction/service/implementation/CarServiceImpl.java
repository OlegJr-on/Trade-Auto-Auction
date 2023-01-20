package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.CarDTO;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.CarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    private final UnitOfWork unitOfWork;

    public CarServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public List<CarDTO> findAll() {
        return null;
    }

    @Override
    public CarDTO findById(int carId) {
        return null;
    }

    @Override
    public List<CarDTO> findByMark(String mark) {
        return null;
    }

    @Override
    public List<CarDTO> findByMarkAndModel(String mark, String model) {
        return null;
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
