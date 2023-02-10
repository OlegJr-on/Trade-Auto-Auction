package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.BidDTO;
import com.auction.auto_auction.entity.*;
import com.auction.auto_auction.exception.OutOfMoneyException;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.exception.TimeLotException;
import com.auction.auto_auction.mapper.BidMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.TradingService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TradingServiceImpl implements TradingService{
    private final UnitOfWork unitOfWork;
    private final BidMapper bidMapper;

    @Override
    public BidDTO getById(int bidId) {

        Bid bidEntity = this.unitOfWork.getBidRepository().findById(bidId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bid","id",String.valueOf(bidId)));

        return this.bidMapper.mapToDTO(bidEntity);
    }

    @Override
    public List<BidDTO> getByCustomerId(int customerId) {

        List<Bid> bidEntities = this.unitOfWork.getBidRepository()
                .findByCustomerId(customerId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found bids by customer id: " + customerId));

        return bidEntities.stream()
                          .map(this.bidMapper::mapToDTO)
                          .toList();
    }

    @Override
    public List<BidDTO> getByLotId(int lotId) {

        List<Bid> bidEntities = this.unitOfWork.getBidRepository()
                .findByLotId(lotId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found bids by lot id: " + lotId));

        return bidEntities.stream()
                          .map(this.bidMapper::mapToDTO)
                          .toList();
    }

    @Override
    public List<BidDTO> getAll() {

        List<Bid> bidsFromSource = this.unitOfWork.getBidRepository().findAll();

        return bidsFromSource.stream()
                             .map(this.bidMapper::mapToDTO)
                             .toList();
    }

    @Override
    public List<BidDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end) {

        List<Bid> bidEntities = this.unitOfWork.getBidRepository()
                .findByOperationDateBetween(start,end)
                    .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Not found bids in date: from %s to %s",start,end)));

        return bidEntities.stream()
                          .map(this.bidMapper::mapToDTO)
                          .toList();
    }

    @Override
    public List<BidDTO> getByDateBefore(LocalDateTime date) {

        List<Bid> bidEntities = this.unitOfWork.getBidRepository()
                .findByOperationDateBefore(date)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found bids before date: " + date));

        return bidEntities.stream()
                           .map(this.bidMapper::mapToDTO)
                           .toList();
    }

    @Override
    public List<BidDTO> getByDateAfter(LocalDateTime date) {

        List<Bid> bidEntities = this.unitOfWork.getBidRepository()
                .findByOperationDateAfter(date)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found bids after date: " + date));

        return bidEntities.stream()
                          .map(this.bidMapper::mapToDTO)
                          .toList();
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void makeBid(int customerId, int lotId, @NotNull BigDecimal bet) {

        // get customer by id from source
        Customer customerWhichMakeBid = this.unitOfWork.getCustomerRepository().findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer","id",String.valueOf(customerId)));

        // get lot by id from source
        Lot lotToWhichBet = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Not found Lot with id: " + lotId));

        if ( !this.isLotTradingJustNow(lotToWhichBet) ) {
            throw new TimeLotException("Current lot isn`t trading just now");
        }

        // checks if customer have enough money for this bet
        if (customerWhichMakeBid.getBankBalance().compareTo(bet) < 0)
            throw new OutOfMoneyException("Customer doesn`t have enough money for make bid");

        this.unitOfWork.getBidRepository()
                .findByLotId(lotId)
                    .ifPresent(bids -> {
                        if (bids.isEmpty()){
                            doFirstBet(bet,customerWhichMakeBid,lotToWhichBet);
                        } else {
                            beatExistingBet(bids,bet,customerWhichMakeBid,lotToWhichBet);
                        }
                    });
    }

    @Transactional
    public void doFirstBet(BigDecimal moneyBet, Customer customerWhichMakeBid, Lot lotToWhichTrading){

        // check if bet is bigger than launchPrice on lot
        if (moneyBet.compareTo(lotToWhichTrading.getLaunchPrice()) < 0){
            throw new OutOfMoneyException("The bet isn`t enough money");
        }

        Bid makeBid = this.buildBidEntity(moneyBet,customerWhichMakeBid,lotToWhichTrading);

        customerWhichMakeBid.addBid(makeBid);
        lotToWhichTrading.addBid(makeBid);

        //save changes
        this.unitOfWork.getBidRepository().save(makeBid);
    }

    @Transactional
    public void beatExistingBet(List<Bid> bidsOnLot, BigDecimal moneyBet,
                                    Customer customerWhichMakeBid, Lot lotToWhichTrading){

        //get last bid for current lot
        Bid lastWinBid = this.getWinBid(bidsOnLot);

        //checks if bet is bigger than "lastBid + minRate"
        BigDecimal lastBet = lastWinBid.getBet();
        BigDecimal currentBetMinusMinRate =  moneyBet.subtract(lotToWhichTrading.getMinRate());

        if (lastBet.compareTo(currentBetMinusMinRate) >= 0)
            throw new OutOfMoneyException("The bet isn`t enough money");

        //set is_active for last bid - false
        lastWinBid.setActive(false);

        Bid makeBid = this.buildBidEntity(moneyBet,customerWhichMakeBid,lotToWhichTrading);

        customerWhichMakeBid.addBid(makeBid);
        lotToWhichTrading.addBid(makeBid);

        //save changes
        this.unitOfWork.getBidRepository().save(lastWinBid);
        this.unitOfWork.getBidRepository().save(makeBid);
    }

    @Override
    @Transactional
    public void editBid(int bidId, @NotNull BidDTO editedBid) {

        // search bid entity by id from data source
        Bid bidEntity = this.unitOfWork.getBidRepository().findById(bidId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bid","id",String.valueOf(bidId)));

        //update info
        bidEntity.setBet(editedBid.getBet());
        bidEntity.setActive(editedBid.isWin());

        // save changes
        this.unitOfWork.getBidRepository().save(bidEntity);
    }

    @Override
    @Transactional
    public void deleteById(int bidId) {

        // search bid entity from data source by id
        Bid bidEntity = this.unitOfWork.getBidRepository().findById(bidId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Bid","id",String.valueOf(bidId)));

        // remove related data on objects
        bidEntity.getLot().removeBid(bidEntity);
        bidEntity.getCustomer().removeBid(bidEntity);

        // delete data by id
        this.unitOfWork.getBidRepository().deleteById(bidEntity.getId());
    }

    @Override
    @Transactional
    public void deleteByPeriod(LocalDateTime from, LocalDateTime to) {

        // search bid entity from data source by date period
        List<Bid> bidEntities = this.unitOfWork.getBidRepository()
                .findByOperationDateBetween(from,to)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    String.format("Not found bids in date: from %s to %s",from,to)));

        // remove related data on objects
        bidEntities.forEach(bid -> bid.getCustomer().removeBid(bid));
        bidEntities.forEach(bid -> bid.getLot().removeBid(bid));

        this.unitOfWork.getBidRepository().deleteAll(bidEntities);
    }

    private Bid buildBidEntity(BigDecimal bet, Customer customer, Lot lot){
        return Bid.builder()
                  .isActive(true)
                  .bet(bet)
                  .operationDate(LocalDateTime.now())
                  .customer(customer)
                  .lot(lot)
                  .build();
    }

    private boolean isLotTradingJustNow(Lot lotToWhichBet){

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime start = lotToWhichBet.getStartTrading();
        LocalDateTime end = lotToWhichBet.getEndTrading();

        return start.isBefore(timeNow) && end.isAfter(timeNow);
    }

    private Bid getWinBid(List<Bid> bids){
        return bids.stream()
                   .filter(Bid::isActive)
                   .findAny()
                   .orElseThrow(() ->
                        new ResourceNotFoundException("Not found winning bid."));
    }
}
