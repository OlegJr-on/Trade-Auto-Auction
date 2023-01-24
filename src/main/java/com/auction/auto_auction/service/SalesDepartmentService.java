package com.auction.auto_auction.service;

import com.auction.auto_auction.dto.SalesDepartmentDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface SalesDepartmentService {

    List<SalesDepartmentDTO> getAll();

    SalesDepartmentDTO getById(int salesId);

    SalesDepartmentDTO getByLotId(int lotId);

    List<SalesDepartmentDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end);

    List<SalesDepartmentDTO> getByDateBefore(LocalDateTime date);

    List<SalesDepartmentDTO> getByDateAfter(LocalDateTime date);

    List<SalesDepartmentDTO> getByLocation(String location);

    void addLotByIdToSaleEvent(int lotId);

    void addLotsToSaleEvent(int... lotIds);

    void create(SalesDepartmentDTO saleDto);

    void update(int saleId,SalesDepartmentDTO saleDto);

    void deleteById(int saleId);

    void deleteLotByIdFromSale(int saleId, int lotId);
}
