package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.BidDTO;
import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Customer;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.enums.LotStatus;
import com.auction.auto_auction.exception.OutOfMoneyException;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.exception.TimeLotException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.TradingService;
import com.auction.auto_auction.utils.ApplicationMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TradingServiceImpl implements TradingService{
    private final UnitOfWork unitOfWork;

    public TradingServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public BidDTO getById(int bidId) {
        return null;
    }

    @Override
    public List<BidDTO> getByCustomerId(int customerId) {
        return null;
    }

    @Override
    public List<BidDTO> getByLotId(int lotId) {
        return null;
    }

    @Override
    public List<BidDTO> getAll() {
        return null;
    }

    @Override
    public List<BidDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public List<BidDTO> getByDateBefore(LocalDateTime date) {
        return null;
    }

    @Override
    public List<BidDTO> getByDateAfter(LocalDateTime date) {
        return null;
    }

    @Override
    public void makeBid(int customerId, int lotId, BigDecimal bet) {

    }

    @Override
    public void editBid(int bidId, BidDTO bidDto) {

    }

    @Override
    public void deleteById(int bidId) {

    }

    @Override
    public void deleteByPeriod(LocalDateTime from, LocalDateTime to) {

    }
}
