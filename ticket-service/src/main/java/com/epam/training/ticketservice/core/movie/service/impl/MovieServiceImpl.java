package com.epam.training.ticketservice.core.movie.service.impl;

import com.epam.training.ticketservice.core.movie.exception.MovieAlreadyExistException;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
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

        if (!movieRepository.existsById(name)) {
            throw new MovieDoesNotExistException(name);
        }

        movieRepository.deleteById(name);
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
