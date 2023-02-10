package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.SalesDepartmentDTO;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.SalesDepartment;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.mapper.SalesMapper;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.SalesDepartmentService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class SalesDepartmentServiceImpl implements SalesDepartmentService {
    private final UnitOfWork unitOfWork;
    private final SalesMapper salesMapper;

    @Override
    public List<SalesDepartmentDTO> getAll() {

        List<SalesDepartment> salesFromSource = this.unitOfWork.getSalesDepartmentRepository().findAll();

        return this.constructSaleDepartmentDTOs(salesFromSource);
    }

    @Override
    public SalesDepartmentDTO getById(int salesId) {

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(salesId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException(
                                SalesDepartment.class.getSimpleName(),"id",String.valueOf(salesId)));

        SalesDepartmentDTO saleDto = this.salesMapper.mapToDTO(saleEntity);

        saleDto.setTimeLeft(generateTimeLeftToEvent(saleEntity.getSalesDate()));

        return saleDto;
    }

    @Override
    public SalesDepartmentDTO getByLotId(int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        SalesDepartment saleEntity = lotEntity.getSalesInfo().stream().findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                SalesDepartment.class.getSimpleName(),"lot.id",String.valueOf(lotId)));

        return this.salesMapper.mapToDTO(saleEntity);
    }

    @Override
    public List<SalesDepartmentDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end) {

        List<SalesDepartment> saleEntities = this.unitOfWork.getSalesDepartmentRepository()
                .findBySalesDateBetween(start,end)
                    .orElseThrow(() ->
                        new ResourceNotFoundException(
                                String.format("Not found sales in date: from %s to %s",start,end)));

        return this.constructSaleDepartmentDTOs(saleEntities);
    }

    @Override
    public List<SalesDepartmentDTO> getByDateBefore(LocalDateTime date) {

        List<SalesDepartment> saleEntities = this.unitOfWork.getSalesDepartmentRepository()
                .findBySalesDateBefore(date)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found sale events before date: " + date));

        return this.constructSaleDepartmentDTOs(saleEntities);
    }

    @Override
    public List<SalesDepartmentDTO> getByDateAfter(LocalDateTime date) {

        List<SalesDepartment> saleEntities = this.unitOfWork.getSalesDepartmentRepository()
                .findBySalesDateAfter(date)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Not found sale events after date: " + date));

        return this.constructSaleDepartmentDTOs(saleEntities);
    }

    @Override
    public List<SalesDepartmentDTO> getByLocation(String location) {

        List<SalesDepartment> salesByLocation = this.unitOfWork.getSalesDepartmentRepository()
                .findByLocation(location)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    SalesDepartment.class.getSimpleName(),"location",location));


        return this.constructSaleDepartmentDTOs(salesByLocation);
    }

    @Override
    @Transactional
    public void addLotByIdToSaleEvent(int saleId, int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(saleId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    SalesDepartment.class.getSimpleName(),"id",String.valueOf(saleId)));

        saleEntity.addLot(lotEntity);
        lotEntity.addSale(saleEntity);

        this.unitOfWork.getSalesDepartmentRepository().save(saleEntity);
    }

    @Override
    public void addLotsToSaleEvent(int saleId, int... lotIds) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void create(@NotNull SalesDepartmentDTO createdSaleEvent) {

        SalesDepartment saleEntity = this.salesMapper.mapToEntity(createdSaleEvent);

        this.unitOfWork.getSalesDepartmentRepository().save(saleEntity);
    }

    @Override
    @Transactional
    public void update(int saleId, @NotNull SalesDepartmentDTO updatedSale) {

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(saleId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    SalesDepartment.class.getSimpleName(),"id",String.valueOf(saleId)));

        saleEntity.setSalesName(updatedSale.getSalesName());
        saleEntity.setSalesDate(updatedSale.getSalesDate());
        saleEntity.setLocation(updatedSale.getLocation());

        this.unitOfWork.getSalesDepartmentRepository().save(saleEntity);
    }

    @Override
    @Transactional
    public void deleteById(int saleId) {

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(saleId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException(
                                SalesDepartment.class.getSimpleName(),"id",String.valueOf(saleId)));

        saleEntity.getLots().forEach(lot -> lot.removeSale(saleEntity));
        saleEntity.getLots().clear();

        this.unitOfWork.getSalesDepartmentRepository().deleteById(saleId);
    }

    @Override
    @Transactional
    public void deleteLotByIdFromSale(int saleId, int lotId) {

        Lot lotEntity = this.unitOfWork.getLotRepository().findById(lotId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Lot","id",String.valueOf(lotId)));

        SalesDepartment saleEntity = this.unitOfWork.getSalesDepartmentRepository()
                .findById(saleId)
                    .orElseThrow(() ->
                        new ResourceNotFoundException("Sales","id",String.valueOf(saleId)));

        saleEntity.removeLot(lotEntity);
        lotEntity.removeSale(saleEntity);

        this.unitOfWork.getSalesDepartmentRepository().save(saleEntity);
    }

    private List<SalesDepartmentDTO> constructSaleDepartmentDTOs(List<SalesDepartment> sales){
        return sales.stream()
                    .map(sale -> {
                        SalesDepartmentDTO dto = this.salesMapper.mapToDTO(sale);
                        dto.setTimeLeft(this.generateTimeLeftToEvent( sale.getSalesDate() ));
                        return dto;
                    })
                    .toList();
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
