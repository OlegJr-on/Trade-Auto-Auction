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
import java.util.Optional;

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

        // check if lot is trading now
        LocalDateTime timeNow = LocalDateTime.now();
        if ( !(lotToWhichBet.getStartTrading().isBefore(timeNow) &&
                        lotToWhichBet.getEndTrading().isAfter(timeNow)) ) {
            throw new TimeLotException("Current lot isn`t trading just now");
        }

        // checks if customer have enough money for this bet
        if (customerWhichMakeBid.getBankAccount().getBalance().compareTo(bet) < 0)
            throw new OutOfMoneyException("Customer doesn`t have enough money for make bid");

        // get bids with lotId
        Optional<List<Bid>> bidsOnLot = this.unitOfWork.getBidRepository().findByLotId(lotId);

        //checks if bids for lot is already exist
        if (!bidsOnLot.get().isEmpty()){
            beatExistingBet(bidsOnLot.get(),bet,customerWhichMakeBid,lotToWhichBet);
        }
        // if bids on lot does not exist, make first bet
        else {
            doFirstBet(bet,customerWhichMakeBid,lotToWhichBet);
        }
    }

    @Transactional
    public void doFirstBet(BigDecimal moneyBet, Customer customerWhichMakeBid, Lot lotToWhichTrading){

        // check if bet is bigger than launchPrice on lot
        if (moneyBet.compareTo(lotToWhichTrading.getLaunchPrice()) < 0){
            throw new OutOfMoneyException("The bet isn`t enough money");
        }

        //create bid
        Bid makeBid = Bid
                        .builder()
                        .isActive(true)
                        .bet(moneyBet)
                        .operationDate(LocalDateTime.now())
                        .customer(customerWhichMakeBid)
                        .lot(lotToWhichTrading)
                        .build();

        // create two-ways relative:
        //  1) with customer;
        customerWhichMakeBid.getBids().add(makeBid);
        //  2) with lot
        lotToWhichTrading.getBids().add(makeBid);

        //save changes
        this.unitOfWork.getBidRepository().save(makeBid);
    }

    @Transactional
    public void beatExistingBet(List<Bid> bidsOnLot, BigDecimal moneyBet,
                                    Customer customerWhichMakeBid, Lot lotToWhichTrading){

        //get last bid for current lot
        Bid lastBid = bidsOnLot.stream()
                               .filter(Bid::isActive)
                               .findAny()
                               .orElseThrow(() ->
                                       new ResourceNotFoundException("Not found last bid on lot."));


        //checks if bet is bigger than "lastBid + minRate"
        BigDecimal lastBet = lastBid.getBet();
        BigDecimal currentBetMinusMinRate =  moneyBet.subtract(lotToWhichTrading.getMinRate());

        if (lastBet.compareTo(currentBetMinusMinRate) >= 0)
            throw new OutOfMoneyException("The bet isn`t enough money");

        //set is_active for last bid - false
        lastBid.setActive(false);

        //create bid
        Bid makeBid = Bid
                        .builder()
                        .isActive(true) // that bet is biggest
                        .bet(moneyBet)
                        .operationDate(LocalDateTime.now())
                        .customer(customerWhichMakeBid)
                        .lot(lotToWhichTrading)
                        .build();

        // create two-ways relative:
        //  1) with customer;
        customerWhichMakeBid.getBids().add(makeBid);
        //  2) with lot
        lotToWhichTrading.getBids().add(makeBid);

        //save changes
        this.unitOfWork.getBidRepository().save(lastBid);
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
        bidEntity.getLot().getBids().remove(bidEntity);
        bidEntity.getCustomer().getBids().remove(bidEntity);

        // delete data by id
        this.unitOfWork.getBidRepository().deleteById(bidEntity.getId());
    }

    @Override
    @Transactional
    public void deleteByPeriod(LocalDateTime from, LocalDateTime to) {

        // search bid entity from data source by date period
        Optional<List<Bid>> bidEntities = this.unitOfWork.getBidRepository()
                .findByOperationDateBetween(from,to);

        if (bidEntities.get().isEmpty()){
            throw new ResourceNotFoundException(
                    String.format("Not found bids in date: from %s to %s",from,to));
        }

        // remove related data on objects
        bidEntities.get().forEach(bid -> bid.getCustomer().getBids().remove(bid));
        bidEntities.get().forEach(bid -> bid.getLot().getBids().remove(bid));

        // delete data by id
        bidEntities.get().forEach(
                bid -> this.unitOfWork.getBidRepository().deleteById(bid.getId())
        );
    }
}
