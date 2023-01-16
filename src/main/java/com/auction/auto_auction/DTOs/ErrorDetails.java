package com.auction.auto_auction.DTOs;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
public class ErrorDetails {
    private Date timestamp;

    private String message;

    private String details;

    private HttpStatus statusCode;


    public ErrorDetails(Date timestamp, String message, String details, HttpStatus statusCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.statusCode = statusCode;
    }
}