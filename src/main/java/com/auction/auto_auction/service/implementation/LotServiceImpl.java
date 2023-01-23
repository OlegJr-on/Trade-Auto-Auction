package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.entity.AutoPhoto;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.enums.AutoState;
import com.auction.auto_auction.entity.enums.LotStatus;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.LotService;
import com.auction.auto_auction.utils.ApplicationMapper;
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

        return lotsFromSource.stream()
                             .map(ApplicationMapper::mapToLotDTO)
                             .toList();
    }

    @Override
    public LotDTO getById(int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        return ApplicationMapper.mapToLotDTO(lotEntity);
    }

    @Override
    public LotDTO getByCarId(int carId) {

        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        Lot lotEntity = carEntity.getLot();

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

        return lotEntities.get().stream()
                                .map(ApplicationMapper::mapToLotDTO)
                                .toList();
    }


    @Override
    public void create(LotDTO createdLot) {

        if (createdLot == null) {
            throw new NullPointerException("Lot doesn`t created, values is null");
        }

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

        // find car by its id, car should be already exist in data source
        Car carEntity = this.unitOfWork.getCarRepository().findById(carId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Car","id",String.valueOf(carId)));

        // set in lot dto mapped car, because for map lot to entity, car should be not null
        createdLot.setCar(ApplicationMapper.mapToCarDTO(carEntity));

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

    }
}
