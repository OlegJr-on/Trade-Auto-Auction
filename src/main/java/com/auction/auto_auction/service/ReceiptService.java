package com.auction.auto_auction.service;

import com.auction.auto_auction.dto.ordering.ReceiptDTO;

public interface ReceiptService {

    ReceiptDTO getOrdersByCustomerId(int customerId);

    void placeAnOrder(int customerId);

    void payOrder(int customerId, int orderId);

    void payAllCustomerOrders(int customerId);

    void cancelOrder(int customerId, int orderId);
}
