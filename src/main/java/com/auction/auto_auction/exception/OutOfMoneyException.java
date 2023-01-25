package com.auction.auto_auction.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OutOfMoneyException extends RuntimeException{

    private BigDecimal money;

    public OutOfMoneyException(String message, BigDecimal money) {
        super(message);
        this.money = money;
    }

    public OutOfMoneyException(String message) {
        super(message);
    }
}
