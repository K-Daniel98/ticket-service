package com.epam.training.ticketservice.core.pricing.service.impl;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricing.exception.PriceComponentAlreadyExistException;
import com.epam.training.ticketservice.core.pricing.exception.PriceComponentDoesNotExistException;
import com.epam.training.ticketservice.core.pricing.model.PriceComponent;
import com.epam.training.ticketservice.core.pricing.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.pricing.service.PriceComponentService;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.utils.formatter.DateTimeFormatterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriceComponentServiceImpl implements PriceComponentService {

    private final PriceComponentRepository priceComponentRepository;
    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final RoomRepository roomRepository;
    private final DateTimeFormatterUtil dateTimeFormatterUtil;

    @Autowired
    public PriceComponentServiceImpl(PriceComponentRepository priceComponentRepository,
                                     MovieRepository movieRepository,
                                     ScreeningRepository screeningRepository,
                                     RoomRepository roomRepository,
                                     DateTimeFormatterUtil dateTimeFormatterUtil) {
        this.priceComponentRepository = priceComponentRepository;
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.roomRepository = roomRepository;
        this.dateTimeFormatterUtil = dateTimeFormatterUtil;
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
        var room = roomRepository.findByName(roomName)
            .orElseThrow(() -> new RoomDoesNotExistException(roomName));

        var priceComponent = priceComponentRepository.getPriceComponentByName(existingPriceComponentName)
            .orElseThrow(() -> new PriceComponentDoesNotExistException(existingPriceComponentName));

        room.setPriceComponent(priceComponent);

        roomRepository.save(room);

        return room;
    }

    @Override
    public Movie attachPriceComponentToMovie(String existingPriceComponentName, String movieName) {
        var movie = movieRepository.findByName(movieName)
            .orElseThrow(() -> new MovieDoesNotExistException(movieName));

        var priceComponent = priceComponentRepository.getPriceComponentByName(existingPriceComponentName)
            .orElseThrow(() -> new PriceComponentDoesNotExistException(existingPriceComponentName));

        movie.setPriceComponent(priceComponent);

        movieRepository.save(movie);

        return movie;
    }

    @Override
    public Screening attachPriceComponentToScreening(String existingPriceComponentName, String movieName,
                                                     String roomName,
                                                     String screeningTime) {
        var room = roomRepository.findByName(roomName)
            .orElseThrow(() -> new RoomDoesNotExistException(roomName));

        var formattedScreeningTime = dateTimeFormatterUtil.fromString(screeningTime);

        var movie = movieRepository.findByName(movieName)
            .orElseThrow(() -> new MovieDoesNotExistException(movieName));

        var screening =
            screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, formattedScreeningTime)
                .orElseThrow(ScreeningDoesNotExistException::new);

        var priceComponent = priceComponentRepository.getPriceComponentByName(existingPriceComponentName)
            .orElseThrow(() -> new PriceComponentDoesNotExistException(existingPriceComponentName));

        screening.setPriceComponent(priceComponent);

        screeningRepository.save(screening);

        return screening;
    }
}
