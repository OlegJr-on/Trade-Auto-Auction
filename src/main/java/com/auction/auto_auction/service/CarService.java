package com.auction.auto_auction.service;

import com.auction.auto_auction.dto.CarDTO;

import java.util.List;

public interface CarService {

    List<CarDTO> findAll();

    CarDTO findById(int carId);

    List<CarDTO> findByMark(String mark);

    List<CarDTO> findByMarkAndModel(String mark,String model);

    void create(CarDTO carDTO);

    void update(int carId, CarDTO carDTO);

    void deleteById(int carId);
}
