package com.auction.auto_auction.controller.department;

import com.auction.auto_auction.dto.BidDTO;
import com.auction.auto_auction.dto.SalesDepartmentDTO;
import com.auction.auto_auction.service.TradingService;
import com.auction.auto_auction.utils.view.BidViews;
import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.List;

@RestController
@RequestMapping("api-auction/department/trade")
public class TradingController {

    private final TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

}
