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
import org.aspectj.apache.bcel.generic.RET;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

        return this.buildReceiptDTO(ordersByCustomer);
    }

    @Override
    @Transactional
    public void placeAnOrder(int customerId) {

        List<Bid> bidsWhichNotOrdered = this.unitOfWork.getBidRepository()
                .findBidsWhoNotOrderedByCustomerId(customerId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Bids","customer id",String.valueOf(customerId)));

        List<Order> newOrders = this.makeOrdersByWinBids(bidsWhichNotOrdered);

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

                        OrdersDetails orderDetail = this.findNotPaidOrderDetailsById(ods,orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("No found the matching receipt"));

                        BankAccount bankAcc = customerWhichPay.getBankAccount();

                        if (orderDetail.getTotalPrice().compareTo(bankAcc.getBalance()) <= 0){

                            bankAcc.setBalance( this.subtractAmountFromBalance( bankAcc.getBalance(),orderDetail.getTotalPrice() ) );
                            orderDetail.setOrderStatus(OrderStatus.PAID);

                        } else {
                            throw new OutOfMoneyException(
                                    "The customer doesn't have enough money to pay the order");
                        }
                        this.unitOfWork.getOrdersDetailsRepository().save(orderDetail);
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

                        BigDecimal generalPay = this.receiptTotalPrice(ods);

                        BankAccount bankAcc = customerWhichPay.getBankAccount();

                        if (generalPay.compareTo(bankAcc.getBalance()) <= 0){

                            bankAcc.setBalance( this.subtractAmountFromBalance( bankAcc.getBalance(),generalPay ) );
                            this.switchFromNotPaidToPaidStatusOrders(ods);

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

        this.findNotPaidOrderDetailsById(customerOrderDetails,orderId)
                .ifPresentOrElse( ordersDetail -> {

                    Order cancelOrder = ordersDetail.getOrder();
                    Lot cancelLot = cancelOrder.getBid().getLot();

                    ordersDetail.setOrderStatus(OrderStatus.CANCELED);
                    cancelOrder.getBid().setActive(false);
                    cancelLot.setLotStatus(LotStatus.OVERDUE);

                    this.unitOfWork.getLotRepository().saveAndFlush(cancelLot);
                    this.unitOfWork.getOrdersDetailsRepository().save(ordersDetail);
                },
                () -> {
                    throw new ResourceNotFoundException("No found the matching receipt");
                });
    }

    private ReceiptDTO buildReceiptDTO(List<OrdersDetails> customerOrdersDetails){
        return ReceiptDTO.builder()
                         .orders(customerOrdersDetails.stream()
                                                      .map(this.orderDetailsMapper::mapToDTO)
                                                      .toList())
                         .total(this.receiptTotalPrice(customerOrdersDetails))
                         .build();
    }

    private Order buildOrderEntity(Bid bid){
        return Order.builder()
                    .bid(bid)
                    .ordersDetails(Collections.singletonList(this.buildOrderDetailsEntity(bid)))
                    .build();
    }

    private OrdersDetails buildOrderDetailsEntity(Bid bid){
        return OrdersDetails.builder()
                            .orderStatus(OrderStatus.NOT_PAID)
                            .auctionRate(ApplicationConstants.DEFAULT_AUCTION_RATE.doubleValue())
                            .totalPrice(this.calculateTotalPriceForOrderDetails(bid))
                            .build();
    }

    private void switchFromNotPaidToPaidStatusOrders(List<OrdersDetails> ordersDetails){
        ordersDetails.stream()
                     .filter(o -> o.getOrderStatus() == OrderStatus.NOT_PAID)
                     .forEach(o -> o.setOrderStatus(OrderStatus.PAID));
    }

    private Optional<OrdersDetails> findNotPaidOrderDetailsById(List<OrdersDetails> ods, int orderId){
        return ods.stream()
                  .filter(o -> o.getId() == orderId && o.getOrderStatus() == OrderStatus.NOT_PAID)
                  .findAny();
    }

    private List<Order> makeOrdersByWinBids(List<Bid> winBids){
        return winBids.stream()
                .map(this::buildOrderEntity)
                .toList();
    }

    private BigDecimal receiptTotalPrice(List<OrdersDetails> customerOrderDetails){
        return customerOrderDetails.stream()
                .filter(od -> od.getOrderStatus() == OrderStatus.NOT_PAID)
                .map(OrdersDetails::getTotalPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add)
                .setScale(3, RoundingMode.CEILING);
    }

    private BigDecimal calculateTotalPriceForOrderDetails(Bid winBid){

        BigDecimal bet = winBid.getBet();
        BigDecimal priceAuctionRate = bet.multiply(ApplicationConstants.DEFAULT_AUCTION_RATE);

        return bet.add(priceAuctionRate);
    }

    private BigDecimal subtractAmountFromBalance(BigDecimal bankAccBalance, BigDecimal amount){
        return bankAccBalance.subtract(amount)
                             .setScale(2,RoundingMode.CEILING);
    }
}
