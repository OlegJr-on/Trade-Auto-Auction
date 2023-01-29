package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.dto.statistic.CustomerStatisticDTO;
import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.OrdersDetails;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.mapper.CustomerMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


@Service
public class CustomerStatisticServiceImpl {
    private final UnitOfWork unitOfWork;
    private final CustomerMapper customerMapper;

    public CustomerStatisticServiceImpl(UnitOfWork unitOfWork, CustomerMapper customerMapper) {
        this.unitOfWork = unitOfWork;
        this.customerMapper = customerMapper;
    }

}
