package com.epam.training.ticketservice.core.screening.service.impl;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.exception.ScreeningBreakViolationException;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.exception.ScreeningOverlapException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class ScreeningServiceImpl implements ScreeningService {

    private static final long delayInMinutes = 10;

    private final ScreeningRepository screeningRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final DateTimeFormatter dateTimeFormatter;

    @Autowired
    public ScreeningServiceImpl(ScreeningRepository screeningRepository, RoomRepository roomRepository,
                                MovieRepository movieRepository, DateTimeFormatter dateTimeFormatter) {
        this.screeningRepository = screeningRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    @Override
    public Optional<Screening> getScreeningByMovieAndRoomAndScreeningTime(Movie movie, Room room,
                                                                          LocalDateTime screeningTime) {
        return screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, screeningTime);
    }

    @Override
    public List<Screening> getScreeningsByMovie(Movie movie) {
        return screeningRepository.findScreeningsByMovie(movie);
    }

    @Override
    public void createScreening(String movieName,
                                String roomName,
                                String screeningTime) {

        var screeningDateTime = LocalDateTime.parse(screeningTime, dateTimeFormatter);

        var movie = movieRepository.findByName(movieName)
            .orElseThrow(() -> new MovieDoesNotExistException(movieName));

        var room = roomRepository.findByName(roomName)
            .orElseThrow(() -> new RoomDoesNotExistException(roomName));

        var screening = new Screening(room, screeningDateTime);
        screening.setMovie(movie);

        screeningRepository.findAll()
            .forEach(entity ->
                entity
                    .getRoom()
                    .getScreenings()
                    .forEach(roomScreening -> {

                        var startTime = screening.getScreeningTime();
                        long startLength = screening.getMovie().getLength();

                        var endTime = roomScreening.getScreeningTime();
                        long endLength = roomScreening.getMovie().getLength();

                        var endWithDelay = endLength + delayInMinutes;

                        if (checkOverlap(
                            startTime,
                            startLength,
                            endTime,
                            endLength)) {
                            throw new ScreeningOverlapException();
                        } else if (checkOverlap(
                            startTime,
                            startLength,
                            endTime,
                            endWithDelay)) {
                            throw new ScreeningBreakViolationException();
                        }
                    })
            );

        room.getScreenings().add(screening);

        roomRepository.save(screening.getRoom());
    }

    @Override
    public void updateScreening(Screening screening) {
        screeningRepository.save(screening);
    }

    @Override
    public void deleteScreening(String movieName, String roomName, String screeningTime) {

        var screeningDateTime = LocalDateTime.parse(screeningTime, dateTimeFormatter);

        var movie = movieRepository.findByName(movieName)
            .orElseThrow(() -> new MovieDoesNotExistException(movieName));

        var room = roomRepository.findByName(roomName)
            .orElseThrow(() -> new RoomDoesNotExistException(roomName));

        var screening = screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, screeningDateTime)
            .orElseThrow(ScreeningDoesNotExistException::new);

        deleteScreening(screening);
    }

    @Override
    public void deleteScreening(Screening screening) {
        var room = screening.getRoom();
        room.getScreenings().remove(screening);

        roomRepository.save(room);

        screeningRepository.delete(screening);
    }

    @Override
    public List<Screening> listScreenings() {
        return screeningRepository.findAll();
    }

    private boolean checkOverlap(LocalDateTime startTime, long length, LocalDateTime otherStartTime, long otherLength) {

        var endTime = startTime.plusMinutes(length);
        var otherEndTime = otherStartTime.plusMinutes(otherLength);

        return endTime.isAfter(otherStartTime) && startTime.isBefore(otherEndTime);
    }
}
