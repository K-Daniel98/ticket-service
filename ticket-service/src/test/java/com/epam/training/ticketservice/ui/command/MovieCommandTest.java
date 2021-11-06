package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.exception.MovieAlreadyExistException;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.ui.command.impl.MovieCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MovieCommandTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieCommand movieCommand;

    @Test
    public void testCreateMovieShouldThrowMovieAlreadyExistException() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;

        var exception = new MovieAlreadyExistException(movieName);
        // When
        Mockito.doThrow(exception).when(movieService).createMovie(movieName, movieType, length);
        // Then
        Assertions.assertEquals(exception.getMessage(), movieCommand.createMovie(movieName, movieType, length));
    }

    @Test
    public void testCreateMovieShouldSucceed() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;
        // When
        Mockito.doNothing().when(movieService).createMovie(movieName, movieType, length);
        // Then
        Assertions.assertNull(movieCommand.createMovie(movieName, movieType, length));
    }

    @Test
    public void testUpdateMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;

        var exception = new MovieDoesNotExistException(movieName);
        // When
        Mockito.doThrow(exception).when(movieService).updateMovie(movieName, movieType, length);
        // Then
        Assertions.assertEquals(exception.getMessage(), movieCommand.updateMovie(movieName, movieType, length));
    }

    @Test
    public void testUpdateMovieShouldSucceed() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;
        // When
        Mockito.doNothing().when(movieService).updateMovie(movieName, movieType, length);
        // Then
        Assertions.assertNull(movieCommand.updateMovie(movieName, movieType, length));
    }

    @Test
    public void testDeleteMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var movieName = "Rambo";;

        var exception = new MovieDoesNotExistException(movieName);
        // When
        Mockito.doThrow(exception).when(movieService).deleteMovie(movieName);
        // Then
        Assertions.assertEquals(exception.getMessage(), movieCommand.deleteMovie(movieName));
    }

    @Test
    public void testDeleteMovieShouldSucceed() {
        // Given
        var movieName = "Rambo";
        // When
        Mockito.doNothing().when(movieService).deleteMovie(movieName);
        // Then
        Assertions.assertNull(movieCommand.deleteMovie(movieName));
    }

    @Test
    public void testListMoviesShouldReturnNonEmptyList() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;

        var movie = new Movie(movieName, movieType, length);
        var anotherMovie = new Movie(movieName, movieType, length);
        // When
        Mockito.when(movieService.listMovies()).thenReturn(List.of(movie, anotherMovie));
        // Then
        Assertions.assertEquals(2, movieCommand.listMovies().size());
    }

    @Test
    public void testListMoviesShouldReturnEmptyList() {
        // Given
        var expected = List.of("There are no movies at the moment");
        // When
        Mockito.when(movieService.listMovies()).thenReturn(List.of());
        // Then
        Assertions.assertEquals(expected, movieCommand.listMovies());
    }

}
