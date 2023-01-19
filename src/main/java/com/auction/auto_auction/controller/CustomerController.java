package com.auction.auto_auction.controller;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api-auction/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(){

        List<CustomerDTO> customers = this.customerService.findAll();

        return ResponseEntity.ok(customers);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") int customerId){

        CustomerDTO customer = this.customerService.findById(customerId);

        return ResponseEntity.ok(customer);
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(@Valid @NotNull @RequestBody CustomerDTO customerDTO) {

        this.customerService.create(customerDTO);

        return new ResponseEntity<>("Customer is created!", HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> updateCustomer(
            @PathVariable("id") int customerId,
            @Valid @NotNull @RequestBody CustomerDTO customerDTO
    ){

        this.customerService.update(customerId,customerDTO);

        return new ResponseEntity<>("Customer is updated!",HttpStatus.OK);
    }
}
