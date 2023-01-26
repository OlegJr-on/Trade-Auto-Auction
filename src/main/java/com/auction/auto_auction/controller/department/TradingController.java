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

    @JsonView(BidViews.BidDetails.class)
    @GetMapping(path = "/bids")
    public ResponseEntity<List<BidDTO>> getAllBids() {

        List<BidDTO> bids = this.tradingService.getAll();

        return ResponseEntity.ok(bids);
    }

    @JsonView(Views.Public.class)
    @GetMapping(path = "/bids-details")
    public ResponseEntity<List<BidDTO>> getAllBidsWithDetails() {

        List<BidDTO> bids = this.tradingService.getAll();

        return ResponseEntity.ok(bids);
    }

    @JsonView(Views.Public.class)
    @GetMapping(path = "bids/{bidId}")
    public ResponseEntity<BidDTO> getBidWithDetailById(@PathVariable("bidId") int bidId) {

        BidDTO bid = this.tradingService.getById(bidId);

        return ResponseEntity.ok(bid);
    }

    @JsonView(Views.Public.class)
    @GetMapping(path = "/customers/{customerId}/bids")
    public ResponseEntity<List<BidDTO>> getBidWithDetailByCustomerId(
            @PathVariable("customerId") int customerId
    ) {

        List<BidDTO> bids = this.tradingService.getByCustomerId(customerId);

        return ResponseEntity.ok(bids);
    }

    @JsonView(Views.Public.class)
    @GetMapping(path = "/lots/{lotId}/bids")
    public ResponseEntity<List<BidDTO>> getBidWithDetailByLotId(
            @PathVariable("lotId") int lotId
    ) {

        List<BidDTO> bids = this.tradingService.getByLotId(lotId);

        return ResponseEntity.ok(bids);
    }
}
