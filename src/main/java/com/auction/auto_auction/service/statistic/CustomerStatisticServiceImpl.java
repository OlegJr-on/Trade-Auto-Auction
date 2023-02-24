package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.dto.statistic.CustomerStatisticDTO;
import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.OrdersDetails;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.mapper.CustomerMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.utils.ApplicationConstants;
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

        return customersWhoMadeBids.stream()
                .map(cus -> CustomerStatisticDTO
                                                .builder()
                                                .customer(this.customerMapper.mapToDTO(cus))
                                                .bidQuantity(cus.getBids().size())
                                                .build())
                .sorted(Comparator.comparingLong(CustomerStatisticDTO::getBidQuantity).reversed())
                .toList();
    }

    @Override
    public List<CustomerDTO> getTop5RichestCustomers() {

        List<Customer> customers = this.unitOfWork.getCustomerRepository()
                .findCustomersWhoseBalanceMoreThanZero()
                    .orElseThrow(ResourceNotFoundException::new);

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

        return customersWhoSpend.stream()
                .map(cus -> CustomerStatisticDTO
                                    .builder()
                                    .customer(this.customerMapper.mapToDTO(cus))
                                    .bidQuantity(cus.getBids().size())
                                    .spendMoney(this.calculateSpendMoneyOfCustomer(cus))
                                    .quantityWinLot(this.calculateQuantityWinLotOfCustomer(cus))
                                    .build())
                .sorted(Comparator.comparing(CustomerStatisticDTO::getSpendMoney).reversed())
                .toList();
    }

    @Override
    public List<CustomerStatisticDTO> getCustomersByAverageGrowthIndicatorForLaunchPrice() {

        List<Customer> customersWithBids = this.unitOfWork.getCustomerRepository()
                .findAllByBidsNotNull()
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found customers who make bids."));

        return customersWithBids.stream()
                .map(cus -> CustomerStatisticDTO.builder()
                                    .customer(this.customerMapper.mapToDTO(cus))
                                    .bidQuantity(cus.getBids().size())
                                    .quantityWinLot(this.calculateQuantityWinLotOfCustomer(cus))
                                    .averageGrowthIndicatorByLaunchPrice(
                                            cus.getBids().stream()
                                                    .filter(Bid::isActive)
                                                    .map(this::getValueOfGrowthBidByLaunchPrice)
                                                    .mapToLong(x -> x)
                                                    .average()
                                                    .orElse(0))
                                    .build())
                .sorted(Comparator.comparing(CustomerStatisticDTO::getAverageGrowthIndicatorByLaunchPrice).reversed())
                .toList();
    }

    @Override
    public List<CustomerStatisticDTO> getCustomersByAvgIndicatorClosenessToNominalPrice() {

        List<Customer> customersWithPaidOrders = this.unitOfWork.getCustomerRepository()
                .findCustomerWithPaidOrders()
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found customers with paid orders."));

        return customersWithPaidOrders.stream()
                .map(cus -> CustomerStatisticDTO.builder()
                                .customer(this.customerMapper.mapToDTO(cus))
                                .bidQuantity(cus.getBids().size())
                                .avgIndicatorClosenessToNominalPrice(
                                        cus.getBids().stream()
                                                .filter(Bid::isActive)
                                                .map(this::getValueOfBidClosenessToNominalPrice)
                                                .mapToDouble(x -> x)
                                                .average()
                                                .orElse(0))
                                .build())
                .sorted(Comparator.comparing(CustomerStatisticDTO::getAvgIndicatorClosenessToNominalPrice).reversed())
                .toList();
    }

    private BigDecimal calculateSpendMoneyOfCustomer(Customer customer){
        return customer.getBids().stream()
                                 .filter(Bid::isActive)
                                 .flatMap(bid -> bid.getOrder().getOrdersDetails().stream())
                                 .map(OrdersDetails::getTotalPrice)
                                 .reduce(BigDecimal.ZERO,BigDecimal::add)
                                 .setScale(2,RoundingMode.CEILING);
    }

    private Long calculateQuantityWinLotOfCustomer(Customer customer){
        return customer.getBids().stream()
                                 .filter(Bid::isActive)
                                 .count();
    }

    private Long getValueOfGrowthBidByLaunchPrice(Bid bid){

        BigDecimal launchPrice = bid.getLot().getLaunchPrice();
        BigDecimal soldPrice = bid.getBet();

        return soldPrice.divide(launchPrice, 3,RoundingMode.CEILING)
                        .multiply(ApplicationConstants.ONE_HUNDRED_PERCENT)
                        .longValue();
    }

    private Double getValueOfBidClosenessToNominalPrice(Bid bid) {

        BigDecimal nominalPriceOfCar = bid.getLot().getCar().getNominalValue();
        BigDecimal bet = bid.getBet();

        return bet.divide(nominalPriceOfCar,5,RoundingMode.HALF_EVEN)
                  .multiply(ApplicationConstants.ONE_HUNDRED_PERCENT)
                  .doubleValue();
    }
}
