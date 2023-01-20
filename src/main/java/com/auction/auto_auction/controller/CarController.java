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
}
