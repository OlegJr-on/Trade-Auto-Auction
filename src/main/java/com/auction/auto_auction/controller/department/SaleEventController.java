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

    @JsonView(SalesViews.WithTimeLeft.class)
    @GetMapping(path = "/info")
    public ResponseEntity<List<SalesDepartmentDTO>> getAllSales() {

        List<SalesDepartmentDTO> sales = this.salesService.getAll();

        return ResponseEntity.ok(sales);
    }

    @JsonView(SalesViews.WithTimeLeft.class)
    @GetMapping(path = "/{saleId}")
    public ResponseEntity<SalesDepartmentDTO> getSaleById(@PathVariable("saleId") int saleId) {

        SalesDepartmentDTO sale = this.salesService.getById(saleId);

        return ResponseEntity.ok(sale);
    }

    @JsonView(SalesViews.LotsDetails.class)
    @GetMapping(path = "/lots/{lotId}")
    public ResponseEntity<SalesDepartmentDTO> getSaleByLotId(@PathVariable("lotId") int lotId) {

        SalesDepartmentDTO sale = this.salesService.getByLotId(lotId);

        return ResponseEntity.ok(sale);
    }

    @JsonView(SalesViews.WithTimeLeft.class)
    @GetMapping("/period")
    public ResponseEntity<List<SalesDepartmentDTO>> getSalesByPeriod(
            @RequestParam(value = "from",required = false) LocalDateTime from,
            @RequestParam(value = "to",required = false) LocalDateTime to
    ){

        List<SalesDepartmentDTO> sales = null;

        // find by period
        if (from != null && to != null){
            sales = this.salesService.getByDatePeriod(from,to);
        }

        // find by date before
        if (from == null && to != null){
            sales = this.salesService.getByDateBefore(to);
        }

        // find by date after
        if (from != null && to == null){
            sales = this.salesService.getByDateAfter(from);
        }

        // if all properties entered wrong - throw exception
        if (sales == null){
            throw new NullPointerException("Entered data is incorrect");
        }

        return ResponseEntity.ok(sales);
    }
}
