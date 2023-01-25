package com.auction.auto_auction.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TimeLotException extends RuntimeException{

    private String error;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TimeLotException(String messageError, LocalDateTime startDate, LocalDateTime endDate) {
        super(messageError + String.format(" Start date: %s | End date: %s",startDate,endDate));
        this.error = messageError;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TimeLotException(String message) {
        super(message);
        this.error = message;
    }
}
