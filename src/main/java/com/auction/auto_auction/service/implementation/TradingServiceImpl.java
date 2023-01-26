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

        Bid bidEntity = this.unitOfWork.getBidRepository().findById(bidId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bid","id",String.valueOf(bidId)));

        return ApplicationMapper.mapToBidDTO(bidEntity);
    }

    @Override
    public List<BidDTO> getByCustomerId(int customerId) {

        Optional<List<Bid>> bidEntities = this.unitOfWork.getBidRepository().findByCustomerId(customerId);

        if (bidEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Not found bids by customer id: " + customerId);
        }

        return bidEntities.get().stream()
                                .map(ApplicationMapper::mapToBidDTO)
                                .toList();
    }

    @Override
    public List<BidDTO> getByLotId(int lotId) {

        Optional<List<Bid>> bidEntities = this.unitOfWork.getBidRepository().findByLotId(lotId);

        if (bidEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Not found bids by lot id: " + lotId);
        }

        return bidEntities.get().stream()
                                .map(ApplicationMapper::mapToBidDTO)
                                .toList();
    }

    @Override
    public List<BidDTO> getAll() {

        List<Bid> bidsFromSource = this.unitOfWork.getBidRepository().findAll();

        if (bidsFromSource.isEmpty()){
            throw new ResourceNotFoundException("Data source is empty");
        }

        return bidsFromSource.stream()
                             .map(ApplicationMapper::mapToBidDTO)
                             .toList();
    }

    @Override
    public List<BidDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end) {

        Optional<List<Bid>> bidEntities = this.unitOfWork.getBidRepository()
                .findByOperationDateBetween(start,end);

        if (bidEntities.get().isEmpty()){
            throw new ResourceNotFoundException(
                    String.format("Not found bids in date: from %s to %s",start,end));
        }

        return bidEntities.get().stream()
                                .map(ApplicationMapper::mapToBidDTO)
                                .toList();
    }

    @Override
    public List<BidDTO> getByDateBefore(LocalDateTime date) {

        Optional<List<Bid>> bidEntities = this.unitOfWork.getBidRepository()
                .findByOperationDateBefore(date);

        if (bidEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Not found bids before date: " + date);
        }

        return bidEntities.get().stream()
                                .map(ApplicationMapper::mapToBidDTO)
                                .toList();
    }

    @Override
    public List<BidDTO> getByDateAfter(LocalDateTime date) {

        Optional<List<Bid>> bidEntities = this.unitOfWork.getBidRepository()
                .findByOperationDateAfter(date);

        if (bidEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Not found bids after date: " + date);
        }

        return bidEntities.get().stream()
                                .map(ApplicationMapper::mapToBidDTO)
                                .toList();
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
