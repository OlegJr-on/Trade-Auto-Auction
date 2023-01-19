package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.entity.BankAccount;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.Role;
import com.auction.auto_auction.entity.User;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.CustomerService;
import com.auction.auto_auction.utils.ApplicationMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerServiceImpl implements CustomerService{
    private final UnitOfWork unitOfWork;

    public CustomerServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public List<CustomerDTO> findAll() {

        List<Customer> customersFromSource = this.unitOfWork.getCustomerRepository().findAll();

        if (customersFromSource.isEmpty()){
            throw new ResourceNotFoundException("Data source is empty");
        }

        return customersFromSource.stream()
                                  .map(ApplicationMapper::mapToCustomerDTO)
                                  .toList();
    }

    @Override
    public CustomerDTO findById(int customerId) {

        Customer customerEntity = this.unitOfWork.getCustomerRepository().findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));

        return ApplicationMapper.mapToCustomerDTO(customerEntity);
    }

    @Override
    public List<CustomerDTO> findByFirstName(String firstName) {

        Optional<List<User>> userEntities = this.unitOfWork.getUserRepository().findByFirstName(firstName);

        if (userEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Customer","firstName",firstName);
        }

        return userEntities.get().stream()
                                 .map(ApplicationMapper::mapToCustomerDTO)
                                 .toList();
    }

    @Override
    public CustomerDTO findByFirstNameAndLastName(String firstName, String lastName) {
        return null;
    }

    @Override
    public CustomerDTO findByEmail(String email) {
        return null;
    }

    @Override
    public void create(CustomerDTO user) {

    }

    @Override
    public void update(int id, CustomerDTO user) {

    }

    @Override
    public void deleteById(int id) {

    }
}
