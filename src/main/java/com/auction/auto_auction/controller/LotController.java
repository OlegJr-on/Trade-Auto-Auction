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
}
