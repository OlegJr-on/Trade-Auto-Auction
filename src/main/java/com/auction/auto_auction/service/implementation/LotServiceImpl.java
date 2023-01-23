package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.entity.Car;
import com.auction.auto_auction.entity.Lot;
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
        return null;
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
    public void create(LotDTO carDTO) {

    }

    @Override
    public void createByExistCarId(int carId, LotDTO lotDTO) {

    }

    @Override
    public void update(int lotId, LotDTO carDTO) {

    }

    @Override
    public void deleteById(int lotId) {

    }
}
