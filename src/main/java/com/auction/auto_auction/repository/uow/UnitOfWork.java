package com.auction.auto_auction.repository.uow;

import com.auction.auto_auction.repository.*;


public interface UnitOfWork {

    AutoPhotoRepository getAutoPhotoRepository();

    BankAccountRepository getBankAccountRepository();

    BidRepository getBidRepository();

    CarRepository getCarRepository();

    CustomerRepository getCustomerRepository();

    LotRepository getLotRepository();

    OrderRepository getOrderRepository();

    OrdersDetailsRepository getOrdersDetailsRepository();

    RoleRepository getRoleRepository();

    SalesDepartmentRepository getSalesDepartmentRepository();

    UserRepository getUserRepository();

}
