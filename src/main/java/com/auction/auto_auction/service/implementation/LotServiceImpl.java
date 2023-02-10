package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.entity.AutoPhoto;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.enums.LotStatus;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.exception.TimeLotException;
import com.auction.auto_auction.mapper.CarMapper;
import com.auction.auto_auction.mapper.LotMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.LotService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LotServiceImpl implements LotService{
    private final UnitOfWork unitOfWork;
    private final LotMapper lotMapper;
    private final CarMapper carMapper;

    @Override
    public List<LotDTO> getAll() {

        List<Lot> lotsFromSource = this.unitOfWork.getLotRepository().findAll();

        return lotsFromSource.stream()
                             .map(this.lotMapper::mapToDTO)
                             .toList();
    }

    @Override
    public LotDTO getById(int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        return this.lotMapper.mapToDTO(lotEntity);
    }

    @Override
    public LotDTO getByCarId(int carId) {

        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        Lot lotEntity = carEntity.getLot();

        return this.lotMapper.mapToDTO(lotEntity);
    }

    @Override
    public List<LotDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end) {

        List<Lot> lotEntities = this.unitOfWork.getLotRepository()
                .findByStartTradingBetween(start,end)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    String.format("Not found lots in date: from %s to %s",start,end)));

        return lotEntities.stream()
                          .map(this.lotMapper::mapToDTO)
                          .toList();
    }

    @Override
    public List<LotDTO> getByDateBefore(LocalDateTime date) {

        List<Lot> lotEntities = this.unitOfWork.getLotRepository()
                .findByStartTradingBefore(date)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found lots before date: " + date));

        return lotEntities.stream()
                          .map(this.lotMapper::mapToDTO)
                          .toList();
    }

    @Override
    public List<LotDTO> getByDateAfter(LocalDateTime date) {

        List<Lot> lotEntities = this.unitOfWork.getLotRepository()
                .findByStartTradingAfter(date)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found lots after date: " + date));

        return lotEntities.stream()
                          .map(this.lotMapper::mapToDTO)
                          .toList();
    }


    @Override
    @Transactional
    public void create(@NotNull LotDTO createdLot) {

        this.checkTradeTimeOfLot(createdLot.getStartTrading(),createdLot.getEndTrading());

        // set default status for just created lot
        createdLot.setLotStatus(LotStatus.NOT_ACTIVE.label);

        Lot lotEntity = this.lotMapper.mapToEntity(createdLot);
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
    @Transactional
    public void createByExistCarId(int carId, @NotNull LotDTO createdLot) {

        this.checkTradeTimeOfLot(createdLot.getStartTrading(),createdLot.getEndTrading());

        // find car by its id, car should be already exist in data source
        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        createdLot.setCar(this.carMapper.mapToDTO(carEntity));
        createdLot.setLotStatus(LotStatus.NOT_ACTIVE.label);

        Lot lotEntity = this.lotMapper.mapToEntity(createdLot);

        carEntity.setLot(lotEntity);

        this.unitOfWork.getLotRepository().save(lotEntity);
    }

    @Override
    @Transactional
    public void update(int lotId, @NotNull LotDTO updatedLot) {

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
    @Transactional
    public void deleteById(int lotId) {

        // search lot entity from data source by id
        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        // get related car entity from lot
        Car carEntity = lotEntity.getCar();

        // get related photos from car entity
        List<AutoPhoto> photoEntities = carEntity.getPhotos();

        this.unitOfWork.getAutoPhotoRepository().deleteAll(photoEntities);
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

        if ( !this.isTradingTimeBetweenOneAndFiveMin(start,end) ){
            throw new TimeLotException(
                    "Lot trading time must be greater than 1 min. and less than 5 min.");
        }
    }

    private boolean isTradingTimeBetweenOneAndFiveMin(LocalDateTime start, LocalDateTime end){
        return end.minusMinutes(1).isBefore(start) || start.plusMinutes(5).isBefore(end);
    }
}
