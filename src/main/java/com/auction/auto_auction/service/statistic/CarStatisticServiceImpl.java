package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.repository.uow.UnitOfWork;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class CarStatisticServiceImpl {
    private UnitOfWork unitOfWork;
}
