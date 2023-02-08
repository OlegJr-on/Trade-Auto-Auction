package com.auction.auto_auction.controller.department;

import com.auction.auto_auction.service.ReceiptService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-auction/department/payment")
@AllArgsConstructor
public class PaymentController {
    private final ReceiptService receiptService;

}
