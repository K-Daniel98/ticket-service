package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    void createMovie(Movie movie);

    void updateMovie(Movie movie);

    void deleteMovie(String name);

    Optional<Movie> getMovieByName(String name);

    boolean exists(String name);

    List<Movie> listMovies();
}
