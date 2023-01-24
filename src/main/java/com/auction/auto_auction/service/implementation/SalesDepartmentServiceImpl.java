package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.dto.SalesDepartmentDTO;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import com.auction.auto_auction.service.SalesDepartmentService;
import org.springframework.stereotype.Service;

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
        return null;
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
