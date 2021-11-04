package com.epam.training.ticketservice.core.pricing.service;

import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.screening.model.Screening;

public interface PriceComponentService {
    void createPriceComponent(String name, long amount);
    Room attachPriceComponentToRoom(String existingPriceComponentName, String roomName);
    Movie attachPriceComponentToMovie(String existingPriceComponentName, String movieName);
    Screening attachPriceComponentToScreening(String existingPriceComponentName, String movieName, String roomName, String screeningTime);
}
