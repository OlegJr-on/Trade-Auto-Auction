package com.auction.auto_auction.service;

import com.auction.auto_auction.dto.ordering.ReceiptDTO;

import java.util.List;

public interface ReceiptService {

    ReceiptDTO getOrdersByCustomerId(int customerId);

    void placeAnOrder(int customerId);

    void payOrder(int customerId, int orderId);

}
