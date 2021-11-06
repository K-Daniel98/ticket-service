package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.pricing.exception.PriceComponentAlreadyExistException;
import com.epam.training.ticketservice.core.pricing.exception.PriceComponentDoesNotExistException;
import com.epam.training.ticketservice.core.pricing.model.BasePrice;
import com.epam.training.ticketservice.core.pricing.service.PriceComponentService;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.utils.formatter.DateTimeFormatterUtil;
import com.epam.training.ticketservice.ui.command.impl.PriceComponentCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ExtendWith(MockitoExtension.class)
public class PriceComponentCommandTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Mock
    private BasePrice basePrice;

    @Mock
    private PriceComponentService priceComponentService;

    @Mock
    private DateTimeFormatterUtil dateTimeFormatterUtil;

    @InjectMocks
    private PriceComponentCommand priceComponentCommand;

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowRoomDoesNotExistException() {
        // Given
        var priceComponentName = "-1500 HUF discount";
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";

        var exception = new RoomDoesNotExistException(roomName);
        // When
        Mockito.when(priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTime))
            .thenThrow(exception);
        // Then
        Assertions.assertEquals(exception.getMessage(),
            priceComponentCommand.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTime));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowMovieDoesNotExistException() {
        // Given
        var priceComponentName = "-1500 HUF discount";
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";

        var exception = new MovieDoesNotExistException(movieName);
        // When
        Mockito.when(priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTime))
            .thenThrow(exception);
        // Then
        Assertions.assertEquals(exception.getMessage(),
            priceComponentCommand.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTime));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowScreeningDoesNotExistException() {
        // Given
        var priceComponentName = "-1500 HUF discount";
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";

        var exception = new ScreeningDoesNotExistException();
        // When
        Mockito.when(priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTime))
            .thenThrow(exception);
        // Then
        Assertions.assertEquals(exception.getMessage(),
            priceComponentCommand.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTime));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowPriceComponentDoesNotExistException() {
        // Given
        var priceComponentName = "-1500 HUF discount";
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";

        var exception = new PriceComponentDoesNotExistException(priceComponentName);
        // When
        Mockito.when(priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTime))
            .thenThrow(exception);
        // Then
        Assertions.assertEquals(exception.getMessage(),
            priceComponentCommand.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTime));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldSucceed() {
        // Given
        var priceComponentName = "-1500 HUF discount";

        var movieName = "Rambo";
        var movieType = "anime";
        var movieLength = 120L;

        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;

        var screeningTimeStr = "2011-11-11 11:11";

        var movie = new Movie(movieName, movieType, movieLength);
        var room = new Room(roomName, rows, cols);

        var screeningTime = LocalDateTime.parse(screeningTimeStr, formatter);

        var screening = new Screening(movie, room, screeningTime);

        var expectedResult =
            String.format("Attached price component '%s' to screening '%s, screened in room %s, at %s'",
                priceComponentName,
                screening.getMovie(),
                screening.getRoom().getName(),
                formatter.format(screeningTime));
        // When
        Mockito.when(dateTimeFormatterUtil.fromLocalDateTime(screeningTime))
            .thenReturn(screeningTimeStr);
        Mockito.when(priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTimeStr))
            .thenReturn(screening);
        // Then
        Assertions.assertEquals(expectedResult,
            priceComponentCommand.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                screeningTimeStr));
    }

    @Test
    public void testUpdateBasePriceShouldSucceed() {
        // Given
        var price = 1900L;
        // When
        Mockito.doNothing().when(basePrice).setAmount(price);
        // Then
        Assertions.assertNull(priceComponentCommand.updateBasePrice(price));
    }

    @Test
    public void testAttachPriceComponentToMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var priceComponentName = "-1500 HUF discount";
        var movieName = "Rambo";

        var exception = new MovieDoesNotExistException(movieName);
        // When
        Mockito.when(priceComponentService.attachPriceComponentToMovie(priceComponentName, movieName))
            .thenThrow(exception);
        // Then
        Assertions.assertEquals(exception.getMessage(),
            priceComponentCommand.attachPriceComponentToMovie(priceComponentName, movieName));
    }

    @Test
    public void testAttachPriceComponentToMovieShouldSucceed() {
        // Given
        var priceComponentName = "-1500 HUF discount";

        var movieName = "Rambo";
        var movieType = "anime";
        var movieLength = 120L;

        var movie = new Movie(movieName, movieType, movieLength);
        // When
        Mockito.when(priceComponentService.attachPriceComponentToMovie(priceComponentName, movieName))
            .thenReturn(movie);
        // Then
        Assertions.assertNull(priceComponentCommand.attachPriceComponentToMovie(priceComponentName, movieName));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldThrowRoomDoesNotExistException() {
        // Given
        var priceComponentName = "-1500 HUF discount";
        var roomName = "Pedersoli";

        var exception = new RoomDoesNotExistException(roomName);
        // When
        Mockito.when(priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName))
            .thenThrow(exception);
        // Then
        Assertions.assertEquals(exception.getMessage(),
            priceComponentCommand.attachPriceComponentToRoom(priceComponentName, roomName));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldSucceed() {
        // Given
        var priceComponentName = "-1500 HUF discount";

        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;

        var room = new Room(roomName, rows, cols);
        // When
        Mockito.when(priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName))
            .thenReturn(room);
        // Then
        Assertions.assertNull(priceComponentCommand.attachPriceComponentToRoom(priceComponentName, roomName));
    }

    @Test
    public void testCreatePriceComponentShouldThrowPriceComponentAlreadyExistException() {
        // Given
        var priceComponentName = "-1500 HUF discount";
        var amount = 1500L;

        var exception = new PriceComponentAlreadyExistException(priceComponentName);
        // When
        Mockito.doThrow(exception).when(priceComponentService).createPriceComponent(priceComponentName,amount);
        // Then
        Assertions.assertEquals(exception.getMessage(),
            priceComponentCommand.createPriceComponent(priceComponentName, amount));
    }

    @Test
    public void testCreatePriceComponentShouldSucceed() {
        // Given
        var priceComponentName = "-1500 HUF discount";
        var amount = 1500L;
        // Then
        Assertions.assertNull(priceComponentCommand.createPriceComponent(priceComponentName, amount));
    }

}
