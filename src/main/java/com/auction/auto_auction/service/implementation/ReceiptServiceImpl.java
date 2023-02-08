package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.ordering.ReceiptDTO;
import com.auction.auto_auction.entity.*;
import com.auction.auto_auction.entity.enums.LotStatus;
import com.auction.auto_auction.entity.enums.OrderStatus;
import com.auction.auto_auction.exception.OutOfMoneyException;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.mapper.OrderDetailsMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.ReceiptService;
import com.auction.auto_auction.utils.ApplicationConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ReceiptServiceImpl implements ReceiptService{
    private final UnitOfWork unitOfWork;
    private final OrderDetailsMapper orderDetailsMapper;

    @Override
    @Transactional
    public ReceiptDTO getOrdersByCustomerId(int customerId) {

        List<OrdersDetails> ordersByCustomer = this.unitOfWork.getOrdersDetailsRepository()
                .findAllOrdersByCustomerId(customerId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));

        return ReceiptDTO.builder()
                         .orders(ordersByCustomer.stream()
                                 .map(this.orderDetailsMapper::mapToDTO)
                                 .toList())
                         .total(ordersByCustomer.stream()
                                 .filter(od -> od.getOrderStatus() == OrderStatus.NOT_PAID)
                                 .map(OrdersDetails::getTotalPrice)
                                 .reduce(BigDecimal.ZERO,BigDecimal::add)
                                 .setScale(3, RoundingMode.CEILING))
                         .build();
    }

    @Override
    @Transactional
    public void placeAnOrder(int customerId) {

        List<Bid> bidsWhichNotOrdered = this.unitOfWork.getBidRepository()
                .findBidsWhoNotOrderedByCustomerId(customerId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Bids","customer id",String.valueOf(customerId)));

        List<Order> newOrders = bidsWhichNotOrdered.stream()
                .map(bid ->Order
                        .builder()
                        .bid(bid)
                        .ordersDetails(Collections.singletonList(
                                OrdersDetails
                                        .builder()
                                        .orderStatus(OrderStatus.NOT_PAID)
                                        .auctionRate(ApplicationConstants.DEFAULT_AUCTION_RATE.doubleValue())
                                        .totalPrice(
                                                bid.getBet().add(
                                                        bid.getBet().multiply(ApplicationConstants.DEFAULT_AUCTION_RATE)
                                                )
                                        )
                                        .build()
                        ))
                        .build())
                .toList();

        // two ways relation
        newOrders.forEach(order -> order.getOrdersDetails().forEach(od -> od.setOrder(order)));

        // save changes
        this.unitOfWork.getOrderRepository().saveAll(newOrders);
    }

    @Override
    @Transactional
    public void payOrder(int customerId, int orderId) {

        Customer customerWhichPay = this.unitOfWork.getCustomerRepository()
                .findById(customerId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));

        this.unitOfWork.getOrdersDetailsRepository()
                .findAllOrdersByCustomerId(customerId)
                    .ifPresentOrElse(ods -> {

                        OrdersDetails order = ods.stream()
                                .filter(o -> o.getId() == orderId && o.getOrderStatus() == OrderStatus.NOT_PAID)
                                .findAny()
                                .orElseThrow(() -> new ResourceNotFoundException("No found the matching receipt"));

                        BankAccount bankAcc = customerWhichPay.getBankAccount();

                        if (order.getTotalPrice()
                                .compareTo(bankAcc.getBalance()) <= 0){

                            bankAcc.setBalance(
                                    bankAcc.getBalance().subtract(order.getTotalPrice()).setScale(2,RoundingMode.CEILING));
                            order.setOrderStatus(OrderStatus.PAID);

                        } else {
                            throw new OutOfMoneyException(
                                    "The customer doesn't have enough money to pay the order");
                        }
                        this.unitOfWork.getOrdersDetailsRepository().save(order);
                        this.unitOfWork.getBankAccountRepository().save(bankAcc);
                    },
                    ()-> {
                        throw new ResourceNotFoundException(
                                "Order details","customer id",String.valueOf(customerId));
                    });
    }

    @Override
    @Transactional
    public void payAllCustomerOrders(int customerId) {

        Customer customerWhichPay = this.unitOfWork.getCustomerRepository()
                .findById(customerId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));

        this.unitOfWork.getOrdersDetailsRepository()
                .findAllOrdersByCustomerId(customerId)
                    .ifPresentOrElse(ods -> {

                        BigDecimal generalPay = ods.stream()
                                .filter(o -> o.getOrderStatus() == OrderStatus.NOT_PAID)
                                .map(OrdersDetails::getTotalPrice)
                                .reduce(BigDecimal.ZERO,BigDecimal::add);

                        BankAccount bankAcc = customerWhichPay.getBankAccount();

                        if (generalPay.compareTo(bankAcc.getBalance()) <= 0){

                            bankAcc.setBalance(bankAcc.getBalance().subtract(generalPay)
                                                                   .setScale(2,RoundingMode.CEILING));
                            ods.stream()
                                    .filter(o -> o.getOrderStatus() == OrderStatus.NOT_PAID)
                                    .forEach(o -> o.setOrderStatus(OrderStatus.PAID));

                        } else {
                            throw new OutOfMoneyException(
                                    "The customer doesn't have enough money to pay the orders");
                        }
                        this.unitOfWork.getOrdersDetailsRepository().saveAll(ods);
                        this.unitOfWork.getBankAccountRepository().save(bankAcc);
                    },
                    () -> {
                        throw new ResourceNotFoundException(
                                "Order details","customer id",String.valueOf(customerId));
                    });
    }

    @Override
    @Transactional
    public void cancelOrder(int customerId, int orderId) {

        List<OrdersDetails> customerOrderDetails = this.unitOfWork.getOrdersDetailsRepository()
                .findAllOrdersByCustomerId(customerId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Order details","customer id",String.valueOf(customerId)));

        customerOrderDetails.stream()
                .filter(od -> od.getId() == orderId && od.getOrderStatus() == OrderStatus.NOT_PAID)
                .findAny()
                .ifPresentOrElse( od -> {
                    // set status into order details -> CANCELED
                    od.setOrderStatus(OrderStatus.CANCELED);

                    // get order and lot for cancel bid and set status onto lot
                    Order cancelOrder = od.getOrder();
                    Lot lot = cancelOrder.getBid().getLot();

                    // make bid invalid -> not_active
                    cancelOrder.getBid().setActive(false);

                    // set onto lot status OVERDUE
                    lot.setLotStatus(LotStatus.OVERDUE);

                    this.unitOfWork.getLotRepository().save(lot);
                    this.unitOfWork.getOrdersDetailsRepository().save(od);
                },
                () -> {
                    throw new ResourceNotFoundException("No found the matching receipt");
                });
    }
}
