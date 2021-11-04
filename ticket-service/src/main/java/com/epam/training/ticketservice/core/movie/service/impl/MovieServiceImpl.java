package com.epam.training.ticketservice.core.movie.service.impl;

import com.epam.training.ticketservice.core.booking.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.exception.MovieAlreadyExistException;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ScreeningRepository screeningRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, ScreeningRepository screeningRepository,
                            BookingRepository bookingRepository, RoomRepository roomRepository) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public void createMovie(Movie movie) {
        var name = movie.getName();
        if (movieRepository.existsById(name)) {
            throw new MovieAlreadyExistException(name);
        }
        movieRepository.save(movie);
    }

    @Override
    public void updateMovie(Movie movie) {
        var name = movie.getName();
        if (!movieRepository.existsById(name)) {
            throw new MovieDoesNotExistException(name);
        }
        movieRepository.save(movie);
    }

    @Override
    public void deleteMovie(String name) {
        var movie = movieRepository.findByName(name)
            .orElseThrow(() -> new MovieDoesNotExistException(name));

        var associatedScreenings = screeningRepository.findScreeningsByMovie(movie);

        for (var screening : associatedScreenings) {
            var associatedBookings = bookingRepository.findBookingsByScreening(screening);
            bookingRepository.deleteAll(associatedBookings);
            screening.getRoom().getScreenings().remove(screening);
            roomRepository.save(screening.getRoom());
            screening.setMovie(null);
            screeningRepository.delete(screening);
        }
        movieRepository.delete(movie);
    }

    @Override
    public Optional<Movie> getMovieByName(String name) {
        return movieRepository.findByName(name);
    }

    @Override
    public boolean exists(String name) {
        return movieRepository.existsById(name);
    }

    @Override
    public List<Movie> listMovies() {
        return movieRepository.findAll();
    }
}
