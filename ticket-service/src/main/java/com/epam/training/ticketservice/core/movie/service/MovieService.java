package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.movie.model.Movie;

import java.util.List;

public interface MovieService {
    void createMovie(String name, String type, long length);

    void updateMovie(String name, String type, long length);

    void deleteMovie(String name);

    boolean exists(String name);

    List<Movie> listMovies();
}
