package com.auction.auto_auction.controller;

import com.auction.auto_auction.dto.CarDTO;
import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.service.CarService;
import com.auction.auto_auction.utils.ApplicationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api-auction/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {

        List<CarDTO> cars = this.carService.findAll();

        return ResponseEntity.ok(cars);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable("id") int carId) {

        CarDTO car = this.carService.findById(carId);

        return ResponseEntity.ok(car);
    }

    @GetMapping(path = "/by")
    public ResponseEntity<List<CarDTO>> getCarsByProperties(
            @RequestParam(value = "mark",defaultValue = ApplicationConstants.EMPTY_STRING) String mark,
            @RequestParam(value = "model",defaultValue = ApplicationConstants.EMPTY_STRING) String model
    ){

        List<CarDTO> cars = null;

        // find only by mark
        if (!mark.isEmpty() && model.isEmpty()){
            cars = this.carService.findByMark(mark);
        }

        // find by mark and model
        if (!mark.isEmpty() && !model.isEmpty()){
            cars = this.carService.findByMarkAndModel(mark,model);
        }

        // if all properties entered wrong - throw exception
        if (cars == null){
            throw new NullPointerException("Entered data is incorrect");
        }

        return ResponseEntity.ok(cars);
    }

    @PostMapping
    public ResponseEntity<String> createCar(@Valid @NotNull @RequestBody CarDTO carDTO) {

        this.carService.create(carDTO);

        return new ResponseEntity<>("Car is created!", HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> updateCar(
            @PathVariable("id") int carId,
            @Valid @NotNull @RequestBody CarDTO updatedCar
    ){

        this.carService.update(carId,updatedCar);

        return new ResponseEntity<>("Car is updated!",HttpStatus.OK);
    }
}
