package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.utils.formatter.DateTimeFormatterUtil;
import com.epam.training.ticketservice.ui.command.impl.ScreeningCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ScreeningCommandTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Mock
    private ScreeningService screeningService;

    @Mock
    private DateTimeFormatterUtil dateTimeFormatterUtil;

    @InjectMocks
    private ScreeningCommand screeningCommand;

    @Test
    public void testCreateScreeningShouldThrowMovieDoesNotExistException() throws IOException {
        // Given
        var movieName = "Rambo";

        var roomName = "Pedersoli";

        var screeningTime = "2011-11-11 11:11";

        var exception = new MovieDoesNotExistException(roomName);
        // When
        Mockito.doThrow(exception).when(screeningService).createScreening(movieName, roomName, screeningTime);
        // Then
        Assertions.assertEquals(exception.getMessage(), screeningCommand.createScreening(movieName, roomName, screeningTime));
    }

    @Test
    public void testCreateScreeningShouldThrowRoomDoesNotExistException() throws IOException {
        // Given
        var movieName = "Rambo";

        var roomName = "Pedersoli";

        var screeningTime = "2011-11-11 11:11";

        var exception = new RoomDoesNotExistException(roomName);
        // When
        Mockito.doThrow(exception).when(screeningService).createScreening(movieName, roomName, screeningTime);
        // Then
        Assertions.assertEquals(exception.getMessage(), screeningCommand.createScreening(movieName, roomName, screeningTime));
    }

    @Test
    public void testCreateScreeningShouldSucceed() throws IOException {
        // Given
        var movieName = "Rambo";

        var roomName = "Pedersoli";

        var screeningTime = "2011-11-11 11:11";
        // When
        Mockito.doNothing().when(screeningService).createScreening(movieName, roomName, screeningTime);
        // Then
        Assertions.assertNull(screeningCommand.createScreening(movieName, roomName, screeningTime));
    }


    @Test
    public void testDeleteScreeningShouldThrowScreeningDoesNotExistException() {
        // Given
        var movieName = "Rambo";

        var roomName = "Pedersoli";

        var screeningTime = "2011-11-11 11:11";

        var exception = new ScreeningDoesNotExistException();
        // When
        Mockito.doThrow(exception).when(screeningService).deleteScreening(movieName, roomName, screeningTime);
        // Then
        Assertions.assertEquals(exception.getMessage(), screeningCommand.deleteScreening(movieName, roomName, screeningTime));
    }

    @Test
    public void testDeleteScreeningShouldSucceed() {
        // Given
        var movieName = "Rambo";

        var roomName = "Pedersoli";

        var screeningTime = "2011-11-11 11:11";
        // When
        Mockito.doNothing().when(screeningService).deleteScreening(movieName, roomName, screeningTime);
        // Then
        Assertions.assertNull(screeningCommand.deleteScreening(movieName, roomName, screeningTime));
    }

    @Test
    public void testListRoomsShouldReturnNonEmptyList() {
        // Given
        var roomName = "Pedersoli";
        var movieName = "Rambo";

        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);
        var screeningTime = "2011-11-11 11:11";
        var screeningDateTime = LocalDateTime.parse(screeningTime, formatter);

        var screening = new Screening(movie, room, screeningDateTime);
        var otherScreening = new Screening(movie, room, screeningDateTime);
        // When
        Mockito.when(screeningService.listScreenings()).thenReturn(List.of(screening, otherScreening));
        // Then
        Assertions.assertEquals(2, screeningCommand.listScreenings().size());
    }

    @Test
    public void testListRoomsShouldReturnEmptyList() {
        // Given
        var expected = List.of("There are no screenings");
        // When
        Mockito.when(screeningService.listScreenings()).thenReturn(List.of());
        // Then
        Assertions.assertEquals(expected, screeningCommand.listScreenings());
    }

}
