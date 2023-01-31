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
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api-auction/department/sales")
@AllArgsConstructor
public class SaleEventController {
    private final SalesDepartmentService salesService;

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

    @JsonView(SalesViews.WithTimeLeft.class)
    @GetMapping("/by")
    public ResponseEntity<List<SalesDepartmentDTO>> getSalesByLocation(
            @RequestParam(value = "location",defaultValue = ApplicationConstants.EMPTY_STRING) String location
    ){

        List<SalesDepartmentDTO> sales = this.salesService.getByLocation(location);

        return ResponseEntity.ok(sales);
    }

    @PostMapping("/{saleId}/lots/{lotId}")
    public ResponseEntity<String> addLotToSaleEvent(
            @PathVariable("saleId") int saleId,
            @PathVariable("lotId") int lotId
    ){

        this.salesService.addLotByIdToSaleEvent(saleId,lotId);

        return new ResponseEntity<>("Lot added to sale successfully!", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createSaleEvent(
            @Valid
            @NotNull
            @JsonView(SalesViews.Public.class)
            @RequestBody SalesDepartmentDTO justCreatedSale
    ){
        this.salesService.create(justCreatedSale);

        return new ResponseEntity<>("Sale event is created!", HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<String> updateSaleEvent(
            @PathVariable("id") int saleId,
            @Valid
            @NotNull
            @JsonView(SalesViews.Public.class)
            @RequestBody SalesDepartmentDTO justUpdatedSale
    ){

        this.salesService.update(saleId,justUpdatedSale);

        return new ResponseEntity<>("Sale event is updated!",HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteSaleEventById(@PathVariable("id") int saleId){

        this.salesService.deleteById(saleId);

        return new ResponseEntity<>(
                                    String.format("Sale event with id: %d is removed!",saleId),
                                    HttpStatus.OK);
    }

    @DeleteMapping(path = "/{saleId}/lots/{lotId}")
    public ResponseEntity<String> deleteLotFromSaleEventById(
            @PathVariable("saleId") int saleId,
            @PathVariable("lotId") int lotId
    ){

        this.salesService.deleteLotByIdFromSale(saleId,lotId);

        return new ResponseEntity<>(
                                    String.format("Lot with id: %d is removed from sale!",lotId),
                                    HttpStatus.OK);
    }
}
