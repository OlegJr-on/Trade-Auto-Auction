package com.auction.auto_auction.job;

import com.auction.auto_auction.entity.Bid;
import com.auction.auto_auction.entity.Lot;
import com.auction.auto_auction.entity.enums.LotStatus;
import com.auction.auto_auction.exception.ResourceNotFoundException;
import com.auction.auto_auction.repository.uow.UnitOfWork;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class UpdateLotStatus {
    private final UnitOfWork unitOfWork;

    @Scheduled(fixedRate = 60000*2) // 2 min
    @Transactional
    public void setStatusForLots(){

        List<Lot> lotsFromSource = this.unitOfWork.getLotRepository()
                .findByLotStatusNotIn(Set.of(LotStatus.OVERDUE))
                    .orElseThrow(ResourceNotFoundException::new);

        LocalDateTime timeNow = LocalDateTime.now();

        for (Lot lotEntity : lotsFromSource) {

            LocalDateTime startTrading = lotEntity.getStartTrading();
            LocalDateTime endTrading = lotEntity.getEndTrading();
            List<Bid> bidsOfGivenLot = this.unitOfWork.getBidRepository()
                    .findByLotId(lotEntity.getId())
                    .orElseThrow(ResourceNotFoundException::new);

            // set status "Trading"
            if (startTrading.isBefore(timeNow) && endTrading.isAfter(timeNow)) {
                lotEntity.setLotStatus(LotStatus.TRADING);
            }

            // set status "Sold out"
            if (endTrading.isBefore(timeNow) &&
                    bidsOfGivenLot.stream().anyMatch(Bid::isActive)) {
                lotEntity.setLotStatus(LotStatus.SOLD_OUT);
            }

            // set status "Overdue"
            if (endTrading.isBefore(timeNow) &&
                    bidsOfGivenLot.stream().noneMatch(Bid::isActive)) {
                lotEntity.setLotStatus(LotStatus.OVERDUE);
            }

            // set status "Not active"
            if (startTrading.isAfter(timeNow)) {
                lotEntity.setLotStatus(LotStatus.NOT_ACTIVE);
            }

            this.unitOfWork.getLotRepository().save(lotEntity);
        }
    }
}
