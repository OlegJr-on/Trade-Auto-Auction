package com.auction.auto_auction.service;

import com.auction.auto_auction.dto.LotDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface LotService {

    List<LotDTO> getAll();

    LotDTO getById(int lotId);

    LotDTO getByCarId(int carId);

    List<LotDTO> getByDatePeriod(LocalDateTime start, LocalDateTime end);

    List<LotDTO> getByDateBefore(LocalDateTime date);

    List<LotDTO> getByDateAfter(LocalDateTime date);

    void create(LotDTO lotDTO);

    void createByExistCarId(int carId, LotDTO lotDTO);

    void update(int lotId, LotDTO lotDTO);

    void deleteById(int lotId);
}
