package com.auction.auto_auction.service;

import com.auction.auto_auction.dto.BidDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TradingService {

    BidDTO getById(int bidId);

    List<BidDTO> getByCustomerId(int customerId);

    List<BidDTO> getByLotId(int lotId);

    List<BidDTO> getAll();

    List<BidDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end);

    List<BidDTO> getByDateBefore(LocalDateTime date);

    List<BidDTO> getByDateAfter(LocalDateTime date);

    void makeBid(int customerId, int lotId, BigDecimal bet);

    void editBid(int bidId, BidDTO bidDto);

    void deleteById(int bidId);

    void deleteByPeriod(LocalDateTime from, LocalDateTime to);
}
