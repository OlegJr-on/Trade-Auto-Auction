package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.dto.statistic.CustomerStatisticDTO;
import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.OrdersDetails;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.mapper.CustomerMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;


@Service
@AllArgsConstructor
public class CustomerStatisticServiceImpl implements CustomerStatisticService{
    private final UnitOfWork unitOfWork;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerStatisticDTO> getMostActivityCustomers() {

        // find customers who made at least one bid
        List<Customer> customersWhoMadeBids = this.unitOfWork.getCustomerRepository()
                .findAllByBidsNotNull()
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found customers who make bids."));

        // create a resulting list
        List<CustomerStatisticDTO> mostActiveCustomers = customersWhoMadeBids
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

        List<Customer> customers = this.unitOfWork.getCustomerRepository()
                .findCustomersWhoseBalanceMoreThanZero()
                    .orElseThrow(ResourceNotFoundException::new);

        // Top 5 richest customers
        return customers.stream()
                        .map(this.customerMapper::mapToDTO)
                        .sorted(Comparator.comparing(CustomerDTO::getBalance).reversed())
                        .limit(5)
                        .toList();
    }

    @Override
    public List<CustomerStatisticDTO> getCustomersWhoMostSpend() {

        // find customers which spend money on lot
        List<Customer> customersWhoSpend = this.unitOfWork.getCustomerRepository()
                .findCustomersWhoMostSpend()
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found customers."));

        List<CustomerStatisticDTO> mostSpendCustomers = customersWhoSpend.stream()
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

    @Override
    public List<CustomerStatisticDTO> getCustomersByAverageGrowthIndicatorForLaunchPrice() {

        List<Customer> customersWithBids = this.unitOfWork.getCustomerRepository()
                .findAllByBidsNotNull()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Not found customers who make bids."));

        List<CustomerStatisticDTO> customerStatistic = customersWithBids.stream()
                .map(cus -> CustomerStatisticDTO
                                    .builder()
                                    .customer(this.customerMapper.mapToDTO(cus))
                                    .bidQuantity(cus.getBids().size())
                                    .quantityWinLot(cus.getBids().stream().filter(Bid::isActive).count())
                                    .averageGrowthIndicatorByLaunchPrice(
                                            cus.getBids().stream()
                                                    .filter(Bid::isActive)
                                                    .map(bid -> {

                                                        BigDecimal launchPrice = bid.getLot().getLaunchPrice();
                                                        BigDecimal finalPrice = bid.getBet();

                                                        return finalPrice.divide(launchPrice, 3,RoundingMode.CEILING)
                                                                         .multiply(BigDecimal.valueOf(100))
                                                                         .longValue();
                                                    })
                                                    .mapToLong(x -> x)
                                                    .average()
                                                    .orElse(0))
                                    .build())
                .sorted(Comparator.comparing(CustomerStatisticDTO::getAverageGrowthIndicatorByLaunchPrice).reversed())
                .toList();

        return customerStatistic;
    }
}
