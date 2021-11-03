package com.epam.training.ticketservice.core.movie.exception;

public class MovieDoesNotExistException extends RuntimeException {

    public MovieDoesNotExistException(String name) {
        super(String.format("Movie '%s' does not exist", name));
    }

}
