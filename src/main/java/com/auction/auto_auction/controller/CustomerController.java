package com.auction.auto_auction.controller;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.service.CustomerService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


}
