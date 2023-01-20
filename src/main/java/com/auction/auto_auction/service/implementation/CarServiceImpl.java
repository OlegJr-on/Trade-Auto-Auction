package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.repository.uow.UnitOfWork;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl {
    private final UnitOfWork unitOfWork;

    public CarServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
}
