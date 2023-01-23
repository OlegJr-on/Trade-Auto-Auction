package com.auction.auto_auction.controller;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.service.LotService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api-auction/lots")
public class LotController {

    private final LotService lotService;

    public LotController(LotService lotService) {
        this.lotService = lotService;
    }

    @GetMapping
    public ResponseEntity<List<LotDTO>> getAllLots() {

        List<LotDTO> lots = this.lotService.getAll();

        return ResponseEntity.ok(lots);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<LotDTO> getLotById(@PathVariable("id") int lotId) {

        LotDTO lot = this.lotService.getById(lotId);

        return ResponseEntity.ok(lot);
    }

    @GetMapping("/period")
    public ResponseEntity<List<LotDTO>> getLotsByPeriod(
            @RequestParam(value = "from",required = false) LocalDateTime from,
            @RequestParam(value = "to",required = false) LocalDateTime to
    ){

        List<LotDTO> lots = null;

        // find by period
        if (from != null && to != null){
            lots = this.lotService.getByDatePeriod(from,to);
        }

        // find by date before
        if (from == null && to != null){
            lots = this.lotService.getByDateBefore(to);
        }

        // find by date after
        if (from != null && to == null){
            lots = this.lotService.getByDateAfter(from);
        }

        // if all properties entered wrong - throw exception
        if (lots == null){
            throw new NullPointerException("Entered data is incorrect");
        }

        return ResponseEntity.ok(lots);
    }

    @PostMapping
    public ResponseEntity<String> createLot(@Valid @NotNull @RequestBody LotDTO justCreatedLot) {

        this.lotService.create(justCreatedLot);

        return new ResponseEntity<>("Lot is created!", HttpStatus.CREATED);
    }
}
