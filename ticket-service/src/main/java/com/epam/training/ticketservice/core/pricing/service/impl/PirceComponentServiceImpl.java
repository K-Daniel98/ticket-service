package com.epam.training.ticketservice.core.pricing.service.impl;

import com.epam.training.ticketservice.configuration.ApplicationConfiguration;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.pricing.exception.PriceComponentAlreadyExistException;
import com.epam.training.ticketservice.core.pricing.exception.PriceComponentDoesNotExistException;
import com.epam.training.ticketservice.core.pricing.model.PriceComponent;
import com.epam.training.ticketservice.core.pricing.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.pricing.service.PriceComponentService;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PirceComponentServiceImpl implements PriceComponentService {

    private final PriceComponentRepository priceComponentRepository;
    private final MovieService movieService;
    private final ScreeningService screeningService;
    private final RoomService roomService;

    @Autowired
    public PirceComponentServiceImpl(PriceComponentRepository priceComponentRepository,
                                     MovieService movieService,
                                     ScreeningService screeningService,
                                     RoomService roomService) {
        this.priceComponentRepository = priceComponentRepository;
        this.movieService = movieService;
        this.screeningService = screeningService;
        this.roomService = roomService;
    }

    @Override
    public void createPriceComponent(String name, long amount) {
        if (priceComponentRepository.existsById(name)) {
            throw new PriceComponentAlreadyExistException(name);
        }

        var priceComponent = new PriceComponent(name, amount);
        priceComponentRepository.save(priceComponent);
    }

    @Override
    public Room attachPriceComponentToRoom(String existingPriceComponentName, String roomName) {
        var room = roomService.getRoomByName(roomName);

        if (room.isEmpty()) {
            throw new RoomDoesNotExistException(roomName);
        }

        var priceComponent = priceComponentRepository.getPriceComponentByName(existingPriceComponentName);

        if (priceComponent.isEmpty()) {
            throw new PriceComponentDoesNotExistException(existingPriceComponentName);
        }

        room.get().setPriceComponent(priceComponent.get());

        roomService.updateRoom(room.get());

        return room.get();
    }

    @Override
    public Movie attachPriceComponentToMovie(String existingPriceComponentName, String movieName) {
        var movie = movieService.getMovieByName(movieName);

        if (movie.isEmpty()) {
            throw new MovieDoesNotExistException(movieName);
        }

        var priceComponent = priceComponentRepository.getPriceComponentByName(existingPriceComponentName);

        if (priceComponent.isEmpty()) {
            throw new PriceComponentDoesNotExistException(existingPriceComponentName);
        }

        movie.get().setPriceComponent(priceComponent.get());

        movieService.updateMovie(movie.get());

        return movie.get();
    }

    @Override
    public Screening attachPriceComponentToScreening(String existingPriceComponentName, String movieName, String roomName,
                                                     String screeningTime) {
        var room = roomService.getRoomByName(roomName);
        var formattedScreeningTime = LocalDateTime.parse(screeningTime, ApplicationConfiguration.formatter);

        if (room.isEmpty()) {
            throw new RoomDoesNotExistException(roomName);
        }

        var movie = movieService.getMovieByName(movieName);

        if (movie.isEmpty()) {
            throw new MovieDoesNotExistException(movieName);
        }

        var screening = screeningService.getScreeningByMovieAndRoomAndScreeningTime(movie.get(), room.get(), formattedScreeningTime);

        if (screening.isEmpty()) {
            throw new ScreeningDoesNotExistException();
        }

        var priceComponent = priceComponentRepository.getPriceComponentByName(existingPriceComponentName);

        if (priceComponent.isEmpty()) {
            throw new PriceComponentDoesNotExistException(existingPriceComponentName);
        }

        screening.get().setPriceComponent(priceComponent.get());

        screeningService.updateScreening(screening.get());

        return screening.get();
    }
}
