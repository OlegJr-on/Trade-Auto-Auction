package com.auction.auto_auction.controller.department;

import com.auction.auto_auction.dto.ordering.ReceiptDTO;
import com.auction.auto_auction.service.ReceiptService;
import com.auction.auto_auction.utils.view.ReceiptViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api-auction/department/ordering")
@AllArgsConstructor
public class OrderingController {
    private final ReceiptService receiptService;

    @JsonView(ReceiptViews.ReceiptDetails.class)
    @GetMapping("/customers/{customerId}/receipt")
    public ResponseEntity<ReceiptDTO> getReceiptByCustomerId(
            @PathVariable("customerId") int customerId
    ){

        ReceiptDTO receipt = this.receiptService.getOrdersByCustomerId(customerId);

        return ResponseEntity.ok(receipt);
    }

    @PostMapping("/customers/{customerId}")
    public ResponseEntity<String> makeOrderForCustomer(
            @PathVariable("customerId") int customerId
    ){
        this.receiptService.placeAnOrder(customerId);

        return new ResponseEntity<>("The order has been created", HttpStatus.CREATED);
    }
}
