package com.auction.auto_auction.service.implementation;

import com.auction.auto_auction.repository.uow.UnitOfWork;
import org.springframework.stereotype.Service;

@Service
public class SalesDepartmentServiceImpl {
    private final UnitOfWork unitOfWork;

    public SalesDepartmentServiceImpl(UnitOfWork unitOfWork) {
        this.unitOfWork = unitOfWork;
    }
}
