package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.entity.BankAccount;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.Role;
import com.auction.auto_auction.entity.User;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.CustomerService;
import com.auction.auto_auction.utils.ApplicationConstants;
import com.auction.auto_auction.utils.ApplicationMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


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

        User entity = this.unitOfWork.getUserRepository().findByFirstNameAndLastName(firstName,lastName)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","firstName and lastName",
                                                                String.format("%s %s",firstName,lastName)));

        return ApplicationMapper.mapToCustomerDTO(entity);
    }

    @Override
    public CustomerDTO findByEmail(String email) {

        User entity = this.unitOfWork.getUserRepository().findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","email",email));

        return ApplicationMapper.mapToCustomerDTO(entity);
    }

    @Override
    public void create(CustomerDTO createdCustomer) {

        if (createdCustomer == null) {
            throw new NullPointerException("Customer don`t created, values is null");
        }

        // take the default role that will be set in new user entity
        Role regUserRole = this.unitOfWork.getRoleRepository().findById(ApplicationConstants.DEFAULT_ROLE_ID)
                .orElseThrow(() ->
                        new NoSuchElementException("Something went wrong, customer role doesn't set"));

        // map dto to entity and set taken role in entity
        User userEntity = ApplicationMapper.mapToUserEntity(createdCustomer);
        userEntity.setRoles(Collections.singletonList(regUserRole));

        // set data to another components of customerDTO
        Customer customerEntity = new Customer();
        customerEntity.setUser(userEntity);

        BankAccount bankAccEntity = new BankAccount();
        bankAccEntity.setCustomer(customerEntity);
        bankAccEntity.setBalance(BigDecimal.ZERO);

        // save new data into data source
        this.unitOfWork.getUserRepository().save(userEntity);
        this.unitOfWork.getCustomerRepository().save(customerEntity);
        this.unitOfWork.getBankAccountRepository().save(bankAccEntity);
    }

    @Override
    public void update(int id, CustomerDTO user) {

    }

    @Override
    public void deleteById(int id) {

    }
}
