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

        List<Customer> customers = this.unitOfWork.getCustomerRepository().findAll();

        if (customers.isEmpty())
        {
            throw new ResourceNotFoundException("Not found customers.");
        }

        // Top 5 richest customers
        List<CustomerDTO> richestCustomers = customers.stream()
                .map(this.customerMapper::mapToDTO)
                .filter(cus -> cus.getBalance().compareTo(BigDecimal.ZERO) > 0)
                .sorted(Comparator.comparing(CustomerDTO::getBalance).reversed())
                .limit(5)
                .toList();

        return richestCustomers;
    }

    @Override
    public List<CustomerStatisticDTO> getCustomersWhoMostSpend() {

        // find customers which spend money on lot
        Optional<List<Customer>> customersWhoSpend = this.unitOfWork.getCustomerRepository()
                .findCustomersWhoMostSpend();

        if (customersWhoSpend.isEmpty())
        {
            throw new ResourceNotFoundException("Not found customers.");
        }

        List<CustomerStatisticDTO> mostSpendCustomers = customersWhoSpend.get().stream()
                .map(cus -> CustomerStatisticDTO
                                    .builder()
                                    .customer(this.customerMapper.mapToDTO(cus))
                                    .bidQuantity(cus.getBids().size())
                                    .spendMoney(
                                            cus.getBids().stream()
                                                    .filter(Bid::isActive)
                                                    .flatMap(bid -> bid.getOrder().getOrdersDetails().stream())
                                                    .map(OrdersDetails::getTotalPrice)
                                                    .reduce(BigDecimal.ZERO,BigDecimal::add)
                                                    .setScale(2)
                                    )
                                    .quantityWinLot(cus.getBids().stream().filter(Bid::isActive).count())
                                    .build())
                .sorted(Comparator.comparing(CustomerStatisticDTO::getSpendMoney).reversed())
                .toList();

        return mostSpendCustomers;
    }

}
