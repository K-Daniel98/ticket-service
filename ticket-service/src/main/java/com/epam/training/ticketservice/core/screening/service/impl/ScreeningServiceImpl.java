package com.epam.training.ticketservice.core.screening.service.impl;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.exception.ScreeningBreakViolationException;
import com.epam.training.ticketservice.core.screening.exception.ScreeningOverlapException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ScreeningServiceImpl implements ScreeningService {

    private static final long delayInMinutes = 10;

    private final ScreeningRepository screeningRepository;
    private final RoomService roomService;

    @Autowired
    public ScreeningServiceImpl(ScreeningRepository screeningRepository, RoomService roomService) {
        this.screeningRepository = screeningRepository;
        this.roomService = roomService;
    }

    @Override
    public Optional<Screening> getScreeningByMovieAndRoomAndScreeningTime(Movie movie, Room room,
                                                                          LocalDateTime screeningTime) {
        return screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, screeningTime);
    }

    @Override
    public void createScreening(Screening screening) {

        screeningRepository.findAll()
            .forEach(entity ->
                entity
                    .getRoom()
                    .getScreenings()
                    .forEach(roomScreening -> {

                        var startTime = screening.getScreeningTime();
                        var startLength = screening.getMovie().getLength();

                        var endTime = roomScreening.getScreeningTime();
                        var endLength = roomScreening.getMovie().getLength();

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

        roomService.updateRoom(screening.getRoom());
    }

    @Override
    public void deleteScreening(Screening screening) {
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
