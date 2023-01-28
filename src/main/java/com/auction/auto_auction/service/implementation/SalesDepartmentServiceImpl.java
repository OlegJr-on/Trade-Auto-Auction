package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.SalesDepartmentDTO;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.SalesDepartment;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.SalesDepartmentService;
import com.auction.auto_auction.utils.ApplicationMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SalesDepartmentServiceImpl implements SalesDepartmentService {
    private final UnitOfWork unitOfWork;

    public SalesDepartmentServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }

    @Override
    public List<SalesDepartmentDTO> getAll() {

        List<SalesDepartment> salesFromSource = this.unitOfWork.getSalesDepartmentRepository().findAll();

        if (salesFromSource.isEmpty()){
            throw new ResourceNotFoundException("Data source is empty");
        }

        List<SalesDepartmentDTO> resultSales = salesFromSource
                        .stream()
                        .map(ApplicationMapper::mapToSalesDepartmentDTO)
                        .peek(saleDto -> saleDto.setTimeLeft(
                                generateTimeLeftToEvent(saleDto.getSalesDate())
                        ))
                        .toList();

        return resultSales;
    }

    @Override
    public SalesDepartmentDTO getById(int salesId) {

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(salesId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sales","id",String.valueOf(salesId)));

        SalesDepartmentDTO saleDto = ApplicationMapper.mapToSalesDepartmentDTO(saleEntity);

        saleDto.setTimeLeft(
                generateTimeLeftToEvent(saleEntity.getSalesDate())
        );

        return saleDto;
    }

    @Override
    public SalesDepartmentDTO getByLotId(int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        SalesDepartment saleEntity = lotEntity.getSalesInfo().stream().findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sales","lot.id",String.valueOf(lotId)));

        return ApplicationMapper.mapToSalesDepartmentDTO(saleEntity);
    }

    @Override
    public List<SalesDepartmentDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end) {

        Optional<List<SalesDepartment>> saleEntities = this.unitOfWork.getSalesDepartmentRepository()
                .findBySalesDateBetween(start,end);

        if (saleEntities.get().isEmpty()){
            throw new ResourceNotFoundException(
                    String.format("Not found sales in date: from %s to %s",start,end));
        }

        return saleEntities.get().stream()
                                 .map(ApplicationMapper::mapToSalesDepartmentDTO)
                                 .peek(saleDto -> saleDto.setTimeLeft(
                                         generateTimeLeftToEvent(saleDto.getSalesDate())
                                 ))
                                 .toList();
    }

    @Override
    public List<SalesDepartmentDTO> getByDateBefore(LocalDateTime date) {

        Optional<List<SalesDepartment>> saleEntities = this.unitOfWork.getSalesDepartmentRepository()
                .findBySalesDateBefore(date);

        if (saleEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Not found sale events before date: " + date);
        }

        return saleEntities.get().stream()
                                 .map(ApplicationMapper::mapToSalesDepartmentDTO)
                                 .peek(saleDto -> saleDto.setTimeLeft(
                                         generateTimeLeftToEvent(saleDto.getSalesDate())
                                 ))
                                 .toList();
    }

    @Override
    public List<SalesDepartmentDTO> getByDateAfter(LocalDateTime date) {

        Optional<List<SalesDepartment>> saleEntities = this.unitOfWork.getSalesDepartmentRepository()
                .findBySalesDateAfter(date);

        if (saleEntities.get().isEmpty()){
            throw new ResourceNotFoundException("Not found sale events after date: " + date);
        }

        return saleEntities.get().stream()
                                 .map(ApplicationMapper::mapToSalesDepartmentDTO)
                                 .peek(saleDto -> saleDto.setTimeLeft(
                                         generateTimeLeftToEvent(saleDto.getSalesDate())
                                 ))
                                 .toList();
    }

    @Override
    public List<SalesDepartmentDTO> getByLocation(String location) {

        List<SalesDepartment> saleEntities = this.unitOfWork.getSalesDepartmentRepository()
                .findAll();

        List<SalesDepartment> salesByLocation = saleEntities.stream()
                .filter(sale -> sale.getLocation().toLowerCase()
                                        .contains(location.toLowerCase()))
                .toList();

        if (salesByLocation.isEmpty()){
            throw new ResourceNotFoundException("Sales","location",location);
        }

        return salesByLocation.stream()
                              .map(ApplicationMapper::mapToSalesDepartmentDTO)
                              .peek(saleDto -> saleDto.setTimeLeft(
                                      generateTimeLeftToEvent(saleDto.getSalesDate())
                              ))
                              .toList();
    }

    @Override
    public void addLotByIdToSaleEvent(int saleId, int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(saleId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Sales","id",String.valueOf(saleId)));

        saleEntity.getLots().add(lotEntity);
        lotEntity.getSalesInfo().add(saleEntity);

        this.unitOfWork.getSalesDepartmentRepository().save(saleEntity);
    }

    @Override
    public void addLotsToSaleEvent(int saleId, int... lotIds) {

    }

    @Override
    public void create(SalesDepartmentDTO createdSaleEvent) {

        if (createdSaleEvent == null) {
            throw new NullPointerException("Sale doesn`t created, values is null");
        }

        SalesDepartment saleEntity = ApplicationMapper.mapToSalesDepartmentEntity(createdSaleEvent);

        this.unitOfWork.getSalesDepartmentRepository().save(saleEntity);
    }

    @Override
    public void update(int saleId, SalesDepartmentDTO updatedSale) {

        if (updatedSale == null) {
            throw new NullPointerException("Sale doesn`t updated, values is null");
        }

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(saleId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Sales","id",String.valueOf(saleId)));

        saleEntity.setSalesName(updatedSale.getSalesName());
        saleEntity.setSalesDate(updatedSale.getSalesDate());
        saleEntity.setLocation(updatedSale.getLocation());

        this.unitOfWork.getSalesDepartmentRepository().save(saleEntity);
    }

    @Override
    public void deleteById(int saleId) {

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(saleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sales","id",String.valueOf(saleId)));

        saleEntity.getLots().forEach(lot -> lot.getSalesInfo().remove(saleEntity));
        saleEntity.getLots().clear();

        this.unitOfWork.getSalesDepartmentRepository().deleteById(saleId);
    }

    @Override
    public void deleteLotByIdFromSale(int saleId, int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(saleId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Sales","id",String.valueOf(saleId)));

        saleEntity.getLots().remove(lotEntity);
        lotEntity.getSalesInfo().remove(saleEntity);

        this.unitOfWork.getSalesDepartmentRepository().save(saleEntity);
    }

    private String generateTimeLeftToEvent(LocalDateTime event){

        LocalDateTime timeNow = LocalDateTime.now();

        Duration period = Duration.between(timeNow, event);

        long days =  Math.max(0, period.toDays());
        period = period.minusDays(days);
        long hours = Math.max(0, period.toHours());
        period = period.minusHours(hours);
        long minutes = Math.max(0, period.toMinutes());

        return String.format("Days: %d | Hours: %d | Minutes: %d", days,hours,minutes);
    }
}
