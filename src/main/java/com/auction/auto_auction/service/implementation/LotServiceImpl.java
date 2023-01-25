package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.entity.AutoPhoto;
import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.enums.LotStatus;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.exception.TimeLotException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.LotService;
import com.auction.auto_auction.utils.ApplicationMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LotServiceImpl implements LotService{
    private final UnitOfWork unitOfWork;

    public LotServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public List<LotDTO> getAll() {

        List<Lot> lotsFromSource = this.unitOfWork.getLotRepository().findAll();

        if (lotsFromSource.isEmpty()){
            throw new ResourceNotFoundException("Data source is empty");
        }

        // on received lot entities re-set status by special conditions
        lotsFromSource.forEach(this::setStatusForLot);

        return lotsFromSource.stream()
                             .map(ApplicationMapper::mapToLotDTO)
                             .toList();
    }

    @Override
    public LotDTO getById(int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        // on received lot entities re-set status by special conditions
        this.setStatusForLot(lotEntity);

        return ApplicationMapper.mapToLotDTO(lotEntity);
    }

    @Override
    public LotDTO getByCarId(int carId) {

        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        Lot lotEntity = carEntity.getLot();

        // on received lot entities re-set status by special conditions
        this.setStatusForLot(lotEntity);

        return ApplicationMapper.mapToLotDTO(lotEntity);
    }

    @Override
    public List<LotDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end) {

        Optional<List<Lot>> lotEntities = this.unitOfWork.getLotRepository()
                .findByStartTradingBetween(start,end);

        if (lotEntities.get().isEmpty()){
            throw new ResourceNotFoundException(
                    String.format("Not found lots in date: from %s to %s",start,end));
        }

        // on received lot entities re-set status by special conditions
        lotEntities.get().forEach(this::setStatusForLot);

        return lotEntities.get().stream()
                                .map(ApplicationMapper::mapToLotDTO)
                                .toList();
    }

    @Override
    public List<LotDTO> getByDateBefore(LocalDateTime date) {

        Optional<List<Lot>> lotEntities = this.unitOfWork.getLotRepository()
                .findByStartTradingBefore(date);

        if (lotEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Not found lots before date: " + date);
        }

        // on received lot entities re-set status by special conditions
        lotEntities.get().forEach(this::setStatusForLot);

        return lotEntities.get().stream()
                                .map(ApplicationMapper::mapToLotDTO)
                                .toList();
    }

    @Override
    public List<LotDTO> getByDateAfter(LocalDateTime date) {

        Optional<List<Lot>> lotEntities = this.unitOfWork.getLotRepository()
                .findByStartTradingAfter(date);

        if (lotEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Not found lots after date: " + date);
        }

        // on received lot entities re-set status by special conditions
        lotEntities.get().forEach(this::setStatusForLot);

        return lotEntities.get().stream()
                                .map(ApplicationMapper::mapToLotDTO)
                                .toList();
    }


    @Override
    public void create(LotDTO createdLot) {

        if (createdLot == null) {
            throw new NullPointerException("Lot doesn`t created, values is null");
        }

        // check trading time of lot, when it`s incorrect - throw exception
        this.checkTradeTimeOfLot(createdLot.getStartTrading(),createdLot.getEndTrading());

        // set default status for just created lot
        createdLot.setLotStatus(LotStatus.NOT_ACTIVE.label);

        // map dto to entity
        Lot lotEntity = ApplicationMapper.mapToLotEntity(createdLot);

        // create relevant entities from given dto
        Car carEntity = lotEntity.getCar();

        List<AutoPhoto> photos = createdLot.getCar().getPhotosSrc()
                                                            .stream()
                                                            .map(AutoPhoto::new)
                                                            .toList();

        // set photos into car entity and in setter act two-ways relation
        carEntity.setPhotos(photos);

        // first of all, save data from car entity
        this.unitOfWork.getCarRepository().save(carEntity);

        //after, set into car entity relevant lot
        carEntity.setLot(lotEntity);

        // finally, save lot entity with other relations in data source
        this.unitOfWork.getLotRepository().save(lotEntity);
    }

    @Override
    public void createByExistCarId(int carId, LotDTO createdLot) {

        if (createdLot == null) {
            throw new NullPointerException("Lot doesn`t created, values is null");
        }

        // check trading time of lot, when it`s incorrect - throw exception
        this.checkTradeTimeOfLot(createdLot.getStartTrading(),createdLot.getEndTrading());

        // find car by its id, car should be already exist in data source
        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        // set in lot dto mapped car, because for map lot to entity, car should be not null
        createdLot.setCar(ApplicationMapper.mapToCarDTO(carEntity));

        // set default status for just created lot
        createdLot.setLotStatus(LotStatus.NOT_ACTIVE.label);

        // map just created lot dto to lot entity
        Lot lotEntity = ApplicationMapper.mapToLotEntity(createdLot);

        // set lot into car entity and in setter act two-ways relation
        carEntity.setLot(lotEntity);

        // save new data
        this.unitOfWork.getLotRepository().save(lotEntity);
    }

    @Override
    public void update(int lotId, LotDTO updatedLot) {

        if (updatedLot == null) {
            throw new NullPointerException("Lot doesn`t updated, entered values is null");
        }

        // check trading time of lot, when it`s incorrect - throw exception
        this.checkTradeTimeOfLot(updatedLot.getStartTrading(),updatedLot.getEndTrading());

        // search lot entity by id from data source
        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        //update info
        lotEntity.setLotStatus(LotStatus.transform(updatedLot.getLotStatus()));
        lotEntity.setLaunchPrice(updatedLot.getLaunchPrice());
        lotEntity.setMinRate(updatedLot.getMinRate());
        lotEntity.setStartTrading(updatedLot.getStartTrading());
        lotEntity.setEndTrading(updatedLot.getEndTrading());

        // save changes
        this.unitOfWork.getLotRepository().save(lotEntity);
    }

    @Override
    public void deleteById(int lotId) {

        // search lot entity from data source by id
        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        // get related car entity from lot
        Car carEntity = lotEntity.getCar();

        // get related photos from car entity
        List<AutoPhoto> photoEntities = carEntity.getPhotos();

        // delete data by ids
        photoEntities.forEach(photo ->
                this.unitOfWork.getAutoPhotoRepository().deleteById(photo.getId()));

        this.unitOfWork.getLotRepository().deleteById(lotEntity.getId());
        this.unitOfWork.getCarRepository().deleteById(carEntity.getId());
    }

    private void checkTradeTimeOfLot(LocalDateTime start, LocalDateTime end){

        // error - when entered date start trading of lot is later than date end trading
        if (start.isAfter(end)){
            throw new TimeLotException(
                    "The date of the start lot trading must not be later than end of lot trading.",
                    start, end);
        }

        // error - when start trading of lot equals end trading of lot
        if (start.compareTo(end) == 0){
            throw new TimeLotException(
                    "The date of the start lot trading must not be equal the end date of lot trading.",
                    start, end);
        }

        // error - when time trading of lot is greater than 1 min. and less than 5 min.
        if (end.minusMinutes(1).isBefore(start) || start.plusMinutes(5).isBefore(end)){
            throw new TimeLotException(
                    "Lot trading time must be greater than 1 min. and less than 5 min.");
        }
    }

    private void setStatusForLot(@NotNull Lot lotEntity){

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime startTrading = lotEntity.getStartTrading();
        LocalDateTime endTrading = lotEntity.getEndTrading();
        List<Bid> bidsOfGivenLot = this.unitOfWork.getBidRepository()
                                                        .findByLotId(lotEntity.getId())
                                                        .get();

        // set status "Trading"
        if (startTrading.isBefore(timeNow) && endTrading.isAfter(timeNow)){
            lotEntity.setLotStatus(LotStatus.TRADING);
        }

        // set status "Sold out"
        if (endTrading.isBefore(timeNow) &&
                bidsOfGivenLot.stream().anyMatch(Bid::isActive)){
            lotEntity.setLotStatus(LotStatus.SOLD_OUT);
        }

        // set status "Overdue"
        if (endTrading.isBefore(timeNow) && bidsOfGivenLot.isEmpty()){
            lotEntity.setLotStatus(LotStatus.OVERDUE);
        }

        // set status "Not active"
        if (startTrading.isAfter(timeNow)){
            lotEntity.setLotStatus(LotStatus.NOT_ACTIVE);
        }

        this.unitOfWork.getLotRepository().save(lotEntity);
    }
}
