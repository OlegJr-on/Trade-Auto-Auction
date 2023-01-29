package com.auction.auto_auction.service.statistic;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.dto.statistic.CustomerStatisticDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerStatisticService {

    List<CustomerStatisticDTO> getMostActivityCustomers();

    List<CustomerDTO> getRichestCustomers();

    List<CustomerStatisticDTO> getCustomersWhoMostSpend();

}
