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
                        .peek(saleDto -> {

                            LocalDateTime timeNow = LocalDateTime.now();
                            LocalDateTime timeSale = saleDto.getSalesDate();

                            Duration period = Duration.between(timeNow, timeSale);

                            long days = period.toDays();
                            period = period.minusDays(days);
                            long hours = period.toHours();
                            period = period.minusHours(hours);
                            long minutes = period.toMinutes();

                            saleDto.setTimeLeft(String.format("Days: %d | Hours: %d | Minutes: %d",
                                    days,hours,minutes));
                        })
                        .toList();

        return resultSales;
    }

    @Override
    public SalesDepartmentDTO getById(int salesId) {
        return null;
    }

    @Override
    public List<SalesDepartmentDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end) {
        return null;
    }

    @Override
    public List<SalesDepartmentDTO> getByDateBefore(LocalDateTime date) {
        return null;
    }

    @Override
    public List<SalesDepartmentDTO> getByDateAfter(LocalDateTime date) {
        return null;
    }

    @Override
    public void addLotByIdToSaleEvent(int lotId) {

    }

    @Override
    public void addLotsToSaleEvent(int... lotIds) {

    }

    @Override
    public void create(SalesDepartmentDTO saleDto) {

    }

    @Override
    public void update(int saleId, SalesDepartmentDTO saleDto) {

    }

    @Override
    public void deleteById(int saleId) {

    }
}
