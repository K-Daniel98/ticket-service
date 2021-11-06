package com.epam.training.ticketservice.core.pricing.service;

import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricing.exception.PriceComponentAlreadyExistException;
import com.epam.training.ticketservice.core.pricing.exception.PriceComponentDoesNotExistException;
import com.epam.training.ticketservice.core.pricing.model.PriceComponent;
import com.epam.training.ticketservice.core.pricing.repository.PriceComponentRepository;
import com.epam.training.ticketservice.core.pricing.service.impl.PriceComponentServiceImpl;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
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
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PricingServiceTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Mock
    private PriceComponentRepository priceComponentRepository;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private DateTimeFormatterUtil dateTimeFormatterUtil;

    @InjectMocks
    private PriceComponentServiceImpl priceComponentService;

    @Test
    public void testCreatePriceComponentShouldThrowPriceComponentAlreadyExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var amount = 1500L;
        // When
        Mockito.when(priceComponentRepository.existsById(priceComponentName)).thenReturn(true);
        // Then
        Assertions.assertThrows(PriceComponentAlreadyExistException.class, () -> priceComponentService.createPriceComponent(priceComponentName, amount));
    }

    @Test
    public void testCreatePriceComponentShouldSucceed() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var amount = 1500L;
        // When
        Mockito.when(priceComponentRepository.existsById(priceComponentName)).thenReturn(false);
        // Then
        Assertions.assertDoesNotThrow(() -> priceComponentService.createPriceComponent(priceComponentName, amount));
    }

    @Test
    public void testAttachPriceComponentToMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var movieName = "Rambo";
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(MovieDoesNotExistException.class, () -> priceComponentService.attachPriceComponentToMovie(priceComponentName, movieName));
    }

    @Test
    public void testAttachPriceComponentToMovieShouldThrowPriceComponentDoesNotExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var movieName = "Rambo";
        var movie = new Movie(movieName, "anime", 120L);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(priceComponentRepository.getPriceComponentByName(priceComponentName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(PriceComponentDoesNotExistException.class, () -> priceComponentService.attachPriceComponentToMovie(priceComponentName, movieName));
    }

    @Test
    public void testAttachPriceComponentToMovieShouldSucceedAndReturnMovie() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var amount = 1500L;
        var movieName = "Rambo";
        var movie = new Movie(movieName, "anime", 120L);
        var priceComponent = new PriceComponent(priceComponentName, amount);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(priceComponentRepository.getPriceComponentByName(priceComponentName)).thenReturn(Optional.of(priceComponent));
        // Then
        Assertions.assertEquals(movie, priceComponentService.attachPriceComponentToMovie(priceComponentName, movieName));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldThrowRoomDoesNotExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var roomName = "Pedersoli";
        // When
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(RoomDoesNotExistException.class, () -> priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldThrowPriceComponentDoesNotExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var roomName = "Pedersoli";
        var room = new Room(roomName, 10, 10);
        // When
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(priceComponentRepository.getPriceComponentByName(priceComponentName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(PriceComponentDoesNotExistException.class, () -> priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName));
    }

    @Test
    public void testAttachPriceComponentToRoomShouldSucceedAndReturnRoom() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var amount = 1500L;
        var roomName = "Pedersoli";
        var room = new Room(roomName, 10, 10);
        var priceComponent = new PriceComponent(priceComponentName, amount);
        // When
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(priceComponentRepository.getPriceComponentByName(priceComponentName)).thenReturn(Optional.of(priceComponent));
        // Then
        Assertions.assertEquals(room, priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName));
    }

    // screening

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowRoomDoesNotExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var roomName = "Pedersoli";
        var movieName = "Rambo";
        var screeningTime = "2011-11-11 11:11";
        // When;
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(RoomDoesNotExistException.class, () -> priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName, screeningTime));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowMovieDoesNotExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var roomName = "Pedersoli";
        var movieName = "Rambo";
        var room = new Room(roomName, 10, 10);
        var screeningTime = "2011-11-11 11:11";
        // When;
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(MovieDoesNotExistException.class, () -> priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName, screeningTime));
    }


    @Test
    public void testAttachPriceComponentToScreeningShouldThrowScreeningDoesNotExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var roomName = "Pedersoli";
        var movieName = "Rambo";
        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);
        var screeningTime = "2011-11-11 11:11";
        var screeningDateTime = LocalDateTime.parse(screeningTime, formatter);
        // When
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(dateTimeFormatterUtil.fromString(screeningTime)).thenReturn(screeningDateTime);
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, screeningDateTime)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(ScreeningDoesNotExistException.class, () -> priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName, screeningTime));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldThrowPriceComponentDoesNotExistException() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var roomName = "Pedersoli";
        var room = new Room(roomName, 10, 10);
        // When
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(priceComponentRepository.getPriceComponentByName(priceComponentName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(PriceComponentDoesNotExistException.class, () -> priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName));
    }

    @Test
    public void testAttachPriceComponentToScreeningShouldSucceedAndReturnScreening() {
        // Given
        var priceComponentName = "1500 HUF voucher";
        var roomName = "Pedersoli";
        var movieName = "Rambo";
        var priceComponent = new PriceComponent(priceComponentName, 1500L);
        var movie = new Movie(movieName, "anime", 120L);
        var room = new Room(roomName, 10, 10);
        var screeningTime = "2011-11-11 11:11";
        var screeningDateTime = LocalDateTime.parse(screeningTime, formatter);
        var screening = new Screening(movie, room, screeningDateTime);
        // When
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(dateTimeFormatterUtil.fromString(screeningTime)).thenReturn(screeningDateTime);
        Mockito.when(priceComponentRepository.getPriceComponentByName(priceComponentName)).thenReturn(Optional.of(priceComponent));
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, screeningDateTime)).thenReturn(Optional.of(screening));
        // Then
        Assertions.assertEquals(screening, priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName, screeningTime));
    }

}
