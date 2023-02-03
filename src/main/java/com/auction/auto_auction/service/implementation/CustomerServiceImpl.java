package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.entity.BankAccount;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.Role;
import com.auction.auto_auction.entity.User;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.mapper.CustomerMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.CustomerService;
import com.auction.auto_auction.utils.ApplicationConstants;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final UnitOfWork unitOfWork;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> findAll() {

        List<Customer> customersFromSource = this.unitOfWork.getCustomerRepository().findAll();

        return customersFromSource.stream()
                                  .map(this.customerMapper::mapToDTO)
                                  .toList();
    }

    @Override
    public CustomerDTO findById(int customerId) {

        Customer customerEntity = this.unitOfWork.getCustomerRepository().findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));

        return this.customerMapper.mapToDTO(customerEntity);
    }

    @Override
    public List<CustomerDTO> findByFirstName(String firstName) {

        List<User> userEntities = this.unitOfWork.getUserRepository()
                .findByFirstName(firstName)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","firstName",firstName));

        return userEntities.stream()
                           .map(this.customerMapper::mapToDTO)
                           .toList();
    }

    @Override
    public CustomerDTO findByFirstNameAndLastName(String firstName, String lastName) {

        User entity = this.unitOfWork.getUserRepository()
                .findByFirstNameAndLastName(firstName,lastName)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","firstName and lastName",
                                                                String.format("%s %s",firstName,lastName)));

        return this.customerMapper.mapToDTO(entity);
    }

    @Override
    public CustomerDTO findByEmail(String email) {

        User entity = this.unitOfWork.getUserRepository()
                .findByEmail(email)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","email",email));

        return this.customerMapper.mapToDTO(entity);
    }

    @Override
    @Transactional
    public void create(@NotNull CustomerDTO createdCustomer) {

        // take the default role that will be set in new user entity
        Role regUserRole = this.unitOfWork.getRoleRepository().findById(ApplicationConstants.DEFAULT_ROLE_ID)
                .orElseThrow(() ->
                        new NoSuchElementException("Something went wrong, customer role doesn't set"));

        // map dto to entity and set taken role in entity
        User userEntity = this.customerMapper.mapToEntity(createdCustomer);
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
    @Transactional
    public void update(int id, @NotNull CustomerDTO updatedCustomer) {

        // search customer entity by id from data source
        Customer customerEntity = this.unitOfWork.getCustomerRepository().findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","id",String.valueOf(id)));

        //on received customer extract user entity that gonna updated
        User userEntity = customerEntity.getUser();

        //update info
        userEntity.setFirstName(updatedCustomer.getFirstName());
        userEntity.setLastName(updatedCustomer.getLastName());
        userEntity.setLocation(updatedCustomer.getLocation());
        userEntity.setPhoneNumber(updatedCustomer.getPhoneNumber());
        userEntity.setBirthDay(updatedCustomer.getBirthDay());
        userEntity.setEmail(updatedCustomer.getEmail());

        // save changes
        this.unitOfWork.getUserRepository().save(userEntity);
    }

    @Override
    @Transactional
    public void deleteById(int customerId) {

        // search customer entity from data source by id
        Customer customerEntity = this.unitOfWork.getCustomerRepository().findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));

        // get related data with customer entity
        User userEntity = customerEntity.getUser();
        BankAccount bankAccEntity = customerEntity.getBankAccount();

        // get all user roles and remove this user from them
        userEntity.getRoles().forEach(role -> role.getUsers().remove(userEntity));

        // delete data by ids
        this.unitOfWork.getBankAccountRepository().deleteById(bankAccEntity.getId());
        this.unitOfWork.getCustomerRepository().deleteById(customerEntity.getId());
        this.unitOfWork.getUserRepository().deleteById(userEntity.getId());
    }
}
