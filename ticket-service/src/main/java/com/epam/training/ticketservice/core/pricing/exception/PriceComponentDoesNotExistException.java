package com.epam.training.ticketservice.core.pricing.exception;

public class PriceComponentDoesNotExistException extends RuntimeException {

    public PriceComponentDoesNotExistException(String priceComponentName) {
        super(String.format("Price component with name '%s' does not exist", priceComponentName));
    }

}
