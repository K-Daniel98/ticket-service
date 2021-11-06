package com.epam.training.ticketservice.ui.command.impl;

import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.ui.command.AbstractUserStateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class MovieCommand extends AbstractUserStateCommand {

    private final MovieService movieService;

    @Autowired
    public MovieCommand(MovieService movieService, AuthService authService) {
        super(authService);
        this.movieService = movieService;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Create a new movie", key = "create movie")
    public String createMovie(@ShellOption String name, @ShellOption String type, @ShellOption long length) {
        try {
            movieService.createMovie(name, type, length);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return null;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Deletes a movie", key = "delete movie")
    public String deleteMovie(@ShellOption String name) {
        try {
            movieService.deleteMovie(name);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return null;
    }

    @ShellMethod(value = "Lists all movies", key = "list movies")
    public List<String> listMovies() {
        var movies = movieService.listMovies();

        if (movies.size() == 0) {
            return List.of("There are no movies at the moment");
        }

        return movies
            .stream()
            .map(Movie::toString)
            .collect(Collectors.toList());
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Updates a movie", key = "update movie")
    public String updateMovie(@ShellOption String name, @ShellOption String type, @ShellOption long length) {
        try {
            movieService.updateMovie(name, type, length);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return null;
    }
}
