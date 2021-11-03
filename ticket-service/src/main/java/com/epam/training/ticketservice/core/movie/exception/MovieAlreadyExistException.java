package com.epam.training.ticketservice.core.movie.exception;

public class MovieAlreadyExistException extends RuntimeException {

    public MovieAlreadyExistException(String name) {
        super(String.format("A movie with name '%s' already exists", name));
    }

}
