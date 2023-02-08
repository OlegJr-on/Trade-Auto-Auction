package com.auction.auto_auction.controller.department;

import com.auction.auto_auction.service.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-auction/department/payment")
@AllArgsConstructor
public class PaymentController {
    private final ReceiptService receiptService;

    @PostMapping("/customers/{customerId}/orders/{orderId}")
    public ResponseEntity<String> payOrder(
            @PathVariable("customerId") int customerId,
            @PathVariable("orderId") int orderId
    ){
        this.receiptService.payOrder(customerId,orderId);

        return new ResponseEntity<>("Order was paid", HttpStatus.OK);
    }
}
