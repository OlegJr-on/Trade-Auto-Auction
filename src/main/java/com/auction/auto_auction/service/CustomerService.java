package com.auction.auto_auction.service;

import com.auction.auto_auction.dto.CustomerDTO;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<CustomerDTO> findAll();

    CustomerDTO findById(int id);

    List<CustomerDTO> findByFirstName(String name);

    CustomerDTO findByFirstNameAndLastName(String firstName,String lastName);

    CustomerDTO findByEmail(String email);

    void create(CustomerDTO user);

    void update(int id,CustomerDTO user);

    void deleteById(int id);
}
