package com.epam.training.ticketservice.core.screening.repository;

import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.screening.model.Screening;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    Optional<Screening> findScreeningByMovieAndRoomAndScreeningTime(Movie movie, Room room,
                                                                    LocalDateTime screeningTime);

    List<Screening> findScreeningsByMovie(Movie movie);

    @Override
    @NonNull
    List<Screening> findAll();
}