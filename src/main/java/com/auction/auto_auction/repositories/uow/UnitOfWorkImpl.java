package com.auction.auto_auction.repositories.uow;

import com.auction.auto_auction.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
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

    @Autowired
    public UnitOfWorkImpl(UserRepository userRepository, CustomerRepository customerRepository,
                          RoleRepository roleRepository, BankAccountRepository bankAccountRepository,
                          CarRepository carRepository, AutoPhotoRepository autoPhotoRepository,
                          LotRepository lotRepository, SalesDepartmentRepository salesDepartmentRepository,
                          BidRepository bidRepository, OrderRepository orderRepository,
                          OrdersDetailsRepository ordersDetailsRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.carRepository = carRepository;
        this.autoPhotoRepository = autoPhotoRepository;
        this.lotRepository = lotRepository;
        this.salesDepartmentRepository = salesDepartmentRepository;
        this.bidRepository = bidRepository;
        this.orderRepository = orderRepository;
        this.ordersDetailsRepository = ordersDetailsRepository;
    }

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
