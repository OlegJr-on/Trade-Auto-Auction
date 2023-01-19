package com.auction.auto_auction.controller;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.service.CustomerService;
import com.auction.auto_auction.utils.ApplicationConstants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("api-auction/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        List<CustomerDTO> customers = this.customerService.findAll();

        return ResponseEntity.ok(customers);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable("id") int customerId) {

        CustomerDTO customer = this.customerService.findById(customerId);

        return ResponseEntity.ok(customer);
    }

    @GetMapping(path = "/by")
    public ResponseEntity<List<CustomerDTO>> getCustomerByProperties(
            @RequestParam(value = "firstName",defaultValue = ApplicationConstants.EMPTY_STRING) String firstName,
            @RequestParam(value = "lastName",defaultValue = ApplicationConstants.EMPTY_STRING) String lastName,
            @RequestParam(value = "email",defaultValue = ApplicationConstants.EMPTY_STRING) String email
    ){

        List<CustomerDTO> customers = null;

        // find by only firstName
        if (!firstName.isEmpty() && lastName.isEmpty()){
            customers = this.customerService.findByFirstName(firstName);
        }

        // find by firstName and lastName
        if (!firstName.isEmpty() && !lastName.isEmpty()){
            customers = Collections.singletonList(
                    this.customerService.findByFirstNameAndLastName(firstName,lastName));
        }

        // find by only email, because email is unique for any customers
        if (!email.isEmpty()){
            customers = Collections.singletonList(
                    this.customerService.findByEmail(email));
        }

        // if all properties entered wrong - throw exception
        if (customers == null){
            throw new NullPointerException("Entered data is incorrect");
        }

        return ResponseEntity.ok(customers);
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

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable("id") int customerId){

        this.customerService.deleteById(customerId);

        return new ResponseEntity<>(
                                    String.format("Customer with id: %d is removed!",customerId),
                                    HttpStatus.OK);
    }
}
