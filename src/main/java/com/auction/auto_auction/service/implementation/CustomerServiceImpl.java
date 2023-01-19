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


@Service
public class CustomerServiceImpl {
    private final UnitOfWork unitOfWork;

    public CustomerServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

}
