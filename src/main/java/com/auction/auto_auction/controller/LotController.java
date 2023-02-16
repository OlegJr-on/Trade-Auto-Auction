package com.auction.auto_auction.controller;

import com.auction.auto_auction.dto.LotDTO;
import com.auction.auto_auction.service.LotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api-auction/lots")
@AllArgsConstructor
public class LotController {
    private final LotService lotService;

    @Operation(summary = "Get all lots",
               responses = {
            @ApiResponse(responseCode = "200", description = "Found all lots in system",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LotDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Lots not found",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<LotDTO>> getAllLots() {

        List<LotDTO> lots = this.lotService.getAll();

        return ResponseEntity.ok(lots);
    }

    @Operation(summary = "Get lot by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot found",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LotDTO.class)) }),
                    @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                                    content = @Content),
                    @ApiResponse(responseCode = "404", description = "Lot doesn`t found",
                                    content = @Content)
            })
    @GetMapping(path = "/{id}")
    public ResponseEntity<LotDTO> getLotById(@PathVariable("id") int lotId) {

        LotDTO lot = this.lotService.getById(lotId);

        return ResponseEntity.ok(lot);
    }

    @Operation(summary = "Get lot by time period",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lots found",
                            content = { @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LotDTO.class)) }),
                    @ApiResponse(responseCode = "400", description = "Invalid time supplied",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found lots by entered period",
                            content = @Content)
            })
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

    @Operation(summary = "Create new lot",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot created successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Entered data is invalid",
                            content = @Content)
            })
    @PostMapping
    public ResponseEntity<String> createLot(@Valid @NotNull @RequestBody LotDTO justCreatedLot) {

        this.lotService.create(justCreatedLot);

        return new ResponseEntity<>("Lot is created!", HttpStatus.CREATED);
    }

    @Operation(summary = "Create new lot by existing car",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot created successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Entered data is invalid",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found car`s id",
                            content = @Content)
            })
    @Parameter(name = "carId", description = "Car`s id which already exist in system")
    @PostMapping("/car/{carId}")
    public ResponseEntity<String> createLotByExistCar(
            @PathVariable("carId") int carId,
            @Valid @NotNull @RequestBody LotDTO justCreatedLot
    ) {

        this.lotService.createByExistCarId(carId,justCreatedLot);

        return new ResponseEntity<>("Lot is created!", HttpStatus.CREATED);
    }

    @Operation(summary = "Update lot by its id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot updated successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "400", description = "Entered data is invalid",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found lot`s id",
                            content = @Content)
            })
    @PutMapping(path = "/{id}")
    public ResponseEntity<String> updateLot(
            @PathVariable("id") int lotId,
            @Valid @NotNull @RequestBody LotDTO updatedLot
    ){

        this.lotService.update(lotId,updatedLot);

        return new ResponseEntity<>("Lot is updated!",HttpStatus.OK);
    }

    @Operation(summary = "Delete lot by its id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lot removed successfully",
                            content = @Content),
                    @ApiResponse(responseCode = "404", description = "Not found lot`s id",
                            content = @Content)
            })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteLotById(@PathVariable("id") int lotId){

        this.lotService.deleteById(lotId);

        return new ResponseEntity<>(
                                    String.format("Lot with id: %d is removed!",lotId),
                                    HttpStatus.OK);
    }
}
