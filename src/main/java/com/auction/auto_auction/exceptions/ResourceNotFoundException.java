package com.auction.auto_auction.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    private String sourceName;
    private String fieldName;
    private String fieldValue;

    public ResourceNotFoundException(String message, String sourceName, String fieldName, String fieldValue) {
        super(message);
        this.sourceName = sourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String sourceName, String fieldName, String fieldValue) {
        super(String.format("In %s not found %s with value: %s",sourceName,fieldName,fieldValue));
        this.sourceName = sourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}