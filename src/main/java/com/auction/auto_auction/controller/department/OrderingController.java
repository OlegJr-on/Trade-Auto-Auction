package com.auction.auto_auction.controller.department;

import com.auction.auto_auction.dto.ordering.ReceiptDTO;
import com.auction.auto_auction.service.ReceiptService;
import com.auction.auto_auction.utils.view.ReceiptViews;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api-auction/department/ordering")
@AllArgsConstructor
public class OrderingController {
    private final ReceiptService receiptService;

}
