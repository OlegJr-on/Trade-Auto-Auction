package com.auction.auto_auction.controller;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(){

        List<CustomerDTO> customers = this.customerService.findAll();

        return ResponseEntity.ok(customers);
    }
}
