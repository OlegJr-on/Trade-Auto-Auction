package com.auction.auto_auction.repository.uow;

import com.auction.auto_auction.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UnitOfWorkImpl implements UnitOfWork{
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final BankAccountRepository bankAccountRepository;
    private final CarRepository carRepository;
    private final AutoPhotoRepository autoPhotoRepository;
    private final LotRepository lotRepository;
    private final SalesDepartmentRepository salesDepartmentRepository;
    private final BidRepository bidRepository;
    private final OrderRepository orderRepository;
    private final OrdersDetailsRepository ordersDetailsRepository;


    @Override
    public AutoPhotoRepository getAutoPhotoRepository() {
        return this.autoPhotoRepository;
    }

    @Override
    public BankAccountRepository getBankAccountRepository() {
        return this.bankAccountRepository;
    }

    @Override
    public BidRepository getBidRepository() {
        return this.bidRepository;
    }

    @Override
    public CarRepository getCarRepository() {
        return this.carRepository;
    }

    @Override
    public CustomerRepository getCustomerRepository() {
        return this.customerRepository;
    }

    @Override
    public LotRepository getLotRepository() {
        return this.lotRepository;
    }

    @Override
    public OrderRepository getOrderRepository() {
        return this.orderRepository;
    }

    @Override
    public OrdersDetailsRepository getOrdersDetailsRepository() {
        return this.ordersDetailsRepository;
    }

    @Override
    public RoleRepository getRoleRepository() {
        return this.roleRepository;
    }

    @Override
    public SalesDepartmentRepository getSalesDepartmentRepository() {
        return this.salesDepartmentRepository;
    }

    @Override
    public UserRepository getUserRepository() {
        return this.userRepository;
    }
}
