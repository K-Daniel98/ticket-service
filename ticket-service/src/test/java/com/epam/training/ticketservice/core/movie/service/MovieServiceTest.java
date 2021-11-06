package com.epam.training.ticketservice.core.movie.service;

import com.epam.training.ticketservice.core.booking.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.exception.MovieAlreadyExistException;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.movie.service.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    public void testCreateMovieShouldThrowMovieAlreadyExistException() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;
        // When
        Mockito.when(movieRepository.existsById(movieName)).thenReturn(true);
        // Then
        Assertions.assertThrows(MovieAlreadyExistException.class,
            () -> movieService.createMovie(movieName, movieType, length));
    }

    @Test
    public void testCreateMovieShouldSucceed() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;
        // When
        Mockito.when(movieRepository.existsById(movieName)).thenReturn(false);
        // Then
        Assertions.assertDoesNotThrow(() -> movieService.createMovie(movieName, movieType, length));
    }

    @Test
    public void testUpdateMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;
        // When
        Mockito.when(movieRepository.existsById(movieName)).thenReturn(false);
        // Then
        Assertions.assertThrows(MovieDoesNotExistException.class, () -> movieService.updateMovie(movieName, movieType, length));
    }

    @Test
    public void testUpdateMovieShouldSucceed() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;
        // When
        Mockito.when(movieRepository.existsById(movieName)).thenReturn(true);
        // Then
        Assertions.assertDoesNotThrow(() -> movieService.updateMovie(movieName, movieType, length));
    }

    @Test
    public void testExistsMovieByName() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;

        var movie = new Movie(movieName, movieType, length);
        // When
        Mockito.when(movieRepository.existsById(movieName)).thenReturn(true);
        // Then
        Assertions.assertTrue(movieService.exists(movieName));;
    }

    @Test
    public void testListMoviesShouldReturnNonEmptyList() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;

        var movie = new Movie(movieName, movieType, length);
        // When
        Mockito.when(movieRepository.findAll()).thenReturn(List.of(movie));
        // Then
        Assertions.assertFalse(movieService.listMovies().isEmpty());
    }

    @Test
    public void testDeleteMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(MovieDoesNotExistException.class, () -> movieService.deleteMovie(movieName));
    }

    @Test
    public void testDeleteMovieShouldSucceed() {
        // Given
        var movieName = "Rambo";
        var movieType = "anime";
        var length = 120L;

        var movie = new Movie(movieName, movieType, length);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        // Then
        Assertions.assertDoesNotThrow(() -> movieService.deleteMovie(movieName));
    }

}
