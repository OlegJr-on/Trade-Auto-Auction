package com.auction.auto_auction.controller.statistic;

import com.auction.auto_auction.dto.CustomerDTO;
import com.auction.auto_auction.dto.statistic.CustomerStatisticDTO;
import com.auction.auto_auction.service.statistic.CustomerStatisticService;
import com.auction.auto_auction.utils.view.CustomerStatisticViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api-auction/statistic/customers")
@AllArgsConstructor
public class CustomerStatisticController {
    private final CustomerStatisticService customerStatisticService;

    @JsonView(CustomerStatisticViews.MostActivityDetails.class)
    @GetMapping("/most-active")
    public ResponseEntity<List<CustomerStatisticDTO>> getMostActiveCustomers(){

        List<CustomerStatisticDTO> customers = this.customerStatisticService.getMostActivityCustomers();

        return ResponseEntity.ok(customers);
    }

    @JsonView(CustomerStatisticViews.MostSpendDetails.class)
    @GetMapping("/most-spend")
    public ResponseEntity<List<CustomerStatisticDTO>> getCustomersWhoMostSpend(){

        List<CustomerStatisticDTO> customers = this.customerStatisticService.getCustomersWhoMostSpend();

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/richest")
    public ResponseEntity<List<CustomerDTO>> getRichestCustomers(){

        List<CustomerDTO> customers = this.customerStatisticService.getRichestCustomers();

        return ResponseEntity.ok(customers);
    }
}
