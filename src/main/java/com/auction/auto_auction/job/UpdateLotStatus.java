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

}
