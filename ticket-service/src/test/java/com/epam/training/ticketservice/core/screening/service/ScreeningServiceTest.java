package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.exception.ScreeningOverlapException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.service.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.utils.formatter.DateTimeFormatterUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ScreeningServiceTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private DateTimeFormatterUtil dateTimeFormatterUtil;

    @InjectMocks
    private ScreeningServiceImpl screeningService;

    @Test
    public void testCreateScreeningWithInvalidMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(MovieDoesNotExistException.class,
            () -> screeningService.createScreening(movieName, roomName, null));
    }

    @Test
    public void testCreateScreeningWithInvalidRoomShouldThrowRoomDoesNotExistException() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var movie = new Movie(movieName, "anime", 120L);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(
            RoomDoesNotExistException.class, () -> screeningService.createScreening(movieName, roomName, null));
    }

    @Test
    public void testCreateScreeningWithOverlappingMovieShouldThrowScreeningOverlapException() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);

        var timeStr = "2011-11-11 11:11";
        var screeningTime = LocalDateTime.parse(timeStr, formatter);

        var screening = new Screening(movie, room, screeningTime);
        room.getScreenings().add(screening);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(dateTimeFormatterUtil.fromString(timeStr)).thenReturn(screeningTime);
        Mockito.when(screeningRepository.findAll()).thenReturn(List.of(screening));
        // Then
        Assertions.assertThrows(
            ScreeningOverlapException.class, () -> screeningService.createScreening(movieName, roomName, timeStr));
    }

    @Test
    public void testCreateScreeningShouldSucceed() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);

        var timeStr = "2011-11-11 11:11";
        var screeningTime = LocalDateTime.parse(timeStr, formatter);

        var nextTimeStr = "2011-11-11 14:11";
        var nextScreeningTime = LocalDateTime.parse(nextTimeStr, formatter);

        var screening = new Screening(movie, room, nextScreeningTime);
        room.getScreenings().add(screening);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(dateTimeFormatterUtil.fromString(timeStr)).thenReturn(screeningTime);
        Mockito.when(screeningRepository.findAll()).thenReturn(List.of(screening));
        // Then
        Assertions.assertDoesNotThrow(() -> screeningService.createScreening(movieName, roomName, timeStr));
    }

    @Test
    public void testDeleteScreeningWithInvalidMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var timeStr = "2011-11-11 11:11";
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(MovieDoesNotExistException.class,
            () -> screeningService.deleteScreening(movieName, roomName, timeStr));
    }

    @Test
    public void testDeleteScreeningWithInvalidRoomShouldThrowRoomDoesNotExistException() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var movie = new Movie(movieName, "anime", 120L);

        var timeStr = "2011-11-11 11:11";
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(RoomDoesNotExistException.class,
            () -> screeningService.deleteScreening(movieName, roomName, timeStr));
    }

    @Test
    public void testDeleteScreeningWithInvalidScreeningShouldThrowScreeningDoesNotExistException() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);

        var timeStr = "2011-11-11 11:11";
        var screeningTime = LocalDateTime.parse(timeStr, formatter);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(dateTimeFormatterUtil.fromString(timeStr)).thenReturn(screeningTime);
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, screeningTime))
            .thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(ScreeningDoesNotExistException.class,
            () -> screeningService.deleteScreening(movieName, roomName, timeStr));
    }

    @Test
    public void testDeleteScreeningShouldSucceed() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);

        var timeStr = "2011-11-11 11:11";
        var screeningTime = LocalDateTime.parse(timeStr, formatter);

        var screening = new Screening(movie, room, screeningTime);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(dateTimeFormatterUtil.fromString(timeStr)).thenReturn(screeningTime);
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, screeningTime))
            .thenReturn(Optional.of(screening));
        // Then
        Assertions.assertDoesNotThrow(() -> screeningService.deleteScreening(movieName, roomName, timeStr));
    }

    @Test
    public void testListScreeningsShouldReturnNonEmptyList() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);

        var timeStr = "2011-11-11 11:11";
        var screeningTime = LocalDateTime.parse(timeStr, formatter);

        var screening = new Screening(movie, room, screeningTime);
        // When
        Mockito.when(screeningRepository.findAll()).thenReturn(List.of(screening));
        // Then
        Assertions.assertFalse(screeningService.listScreenings().isEmpty());
    }

    @Test
    public void testUpdateScreeningShouldSucceed() {
        // Given
        var movieName = "Rambo";
        var roomName = "Pedersoli";

        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);

        var timeStr = "2011-11-11 11:11";
        var screeningTime = LocalDateTime.parse(timeStr, formatter);

        var screening = new Screening(movie, room, screeningTime);
        // Then
        Assertions.assertDoesNotThrow(() -> screeningService.updateScreening(screening));
    }

}
