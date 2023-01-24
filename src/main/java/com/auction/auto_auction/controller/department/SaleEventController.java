package com.auction.auto_auction.controller.department;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.dto.SalesDepartmentDTO;
import com.auction.auto_auction.service.SalesDepartmentService;
import com.auction.auto_auction.utils.ApplicationConstants;
import com.auction.auto_auction.utils.view.SalesViews;
import com.auction.auto_auction.utils.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api-auction/department/sales")
public class SaleEventController {

    private final SalesDepartmentService salesService;

    public SaleEventController(SalesDepartmentService salesService) {
        this.salesService = salesService;
    }

    @JsonView(SalesViews.LotsDetails.class)
    @GetMapping
    public ResponseEntity<List<SalesDepartmentDTO>> getAllSalesWithLots() {

        List<SalesDepartmentDTO> sales = this.salesService.getAll();

        return ResponseEntity.ok(sales);
    }
}
