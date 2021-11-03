package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.screening.model.Screening;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningService {

    Optional<Screening> getScreeningByMovieAndRoomAndScreeningTime(Movie movie, Room room, LocalDateTime screeningTime);

    void createScreening(Screening screening);

    void deleteScreening(Screening screening);

    List<Screening> listScreenings();

}
