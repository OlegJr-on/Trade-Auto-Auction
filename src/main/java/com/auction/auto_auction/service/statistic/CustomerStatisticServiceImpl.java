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
public class CustomerStatisticServiceImpl implements CustomerStatisticService{
    private final UnitOfWork unitOfWork;
    private final CustomerMapper customerMapper;

    public CustomerStatisticServiceImpl(UnitOfWork unitOfWork, CustomerMapper customerMapper) {
        this.unitOfWork = unitOfWork;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerStatisticDTO> getMostActivityCustomers() {

        // find customers who made at least one bid
        Optional<List<Customer>> customersWhoMadeBids = this.unitOfWork.getCustomerRepository()
                .findAllByBidsNotNull();

        if (customersWhoMadeBids.get().isEmpty())
        {
            throw new ResourceNotFoundException("Not found customers who make bids.");
        }

        // create a resulting list
        List<CustomerStatisticDTO> mostActiveCustomers = customersWhoMadeBids.get()
                .stream()
                .map(cus -> CustomerStatisticDTO
                                                .builder()
                                                .customer(this.customerMapper.mapToDTO(cus))
                                                .bidQuantity(cus.getBids().size())
                                                .build())
                .sorted(Comparator.comparingLong(CustomerStatisticDTO::getBidQuantity).reversed())
                .toList();

        return mostActiveCustomers;
    }

    @Override
    public List<CustomerDTO> getRichestCustomers() {
        return null;
    }

    @Override
    public List<CustomerStatisticDTO> getCustomersWhoMostSpend() {
        return null;
    }
}
