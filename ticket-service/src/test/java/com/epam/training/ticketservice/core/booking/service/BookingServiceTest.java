package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.booking.exception.SeatAlreadyTakenException;
import com.epam.training.ticketservice.core.booking.exception.SeatDoesNotExistException;
import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.booking.service.impl.BookingServiceImpl;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricing.model.BasePrice;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.repository.UserRepository;
import com.epam.training.ticketservice.core.user.service.UserService;
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
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserService userService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BasePrice basePrice;

    @Mock
    private DateTimeFormatterUtil dateTimeFormatterUtil;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    public void testBookWithNonExistingMovieShouldThrowMovieDoesNotExistException() {
        // Given
        var user = new User("TestUser", "password", User.Role.USER);
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";
        var seats = "1,1 2,3 5,7";
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(MovieDoesNotExistException.class,
            () -> bookingService.book(user, movieName, roomName, screeningTime, seats));
    }

    @Test
    public void testBookWithNonExistingRoomShouldThrowRoomDoesNotExistException() {
        // Given
        var user = new User("TestUser", "password", User.Role.USER);
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";
        var seats = "1,1 2,3 5,7";

        var movie = new Movie("Rambo", "anime", 150L);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(
            RoomDoesNotExistException.class,
            () -> bookingService.book(user, movieName, roomName, screeningTime, seats));
    }

    @Test
    public void testBookWithNonExistingScreeningShouldThrowScreeningDoesNotExistException() {
        // Given
        var user = new User("TestUser", "password", User.Role.USER);
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";
        var seats = "1,1 2,3 5,7";

        var movie = new Movie("Rambo", "anime", 150L);
        var room = new Room("Pedersoli", 10, 10);

        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, null))
            .thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(
            ScreeningDoesNotExistException.class,
            () -> bookingService.book(user, movieName, roomName, screeningTime, seats));
    }

    @Test
    public void testBookWithValidDataShouldReturnBookedSeatsSet() {
        // Given
        var user = new User("TestUser", "password", User.Role.USER);
        var price = 1500L;
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";
        var seats = "1,1 2,3 5,7";

        var movie = new Movie("Rambo", "anime", 150L);
        var room = new Room("Pedersoli", 10, 10);
        var screening = new Screening(movie, room, LocalDateTime.parse("2011-11-11 11:11", formatter));

        var bookings = Set.of(
            new Booking(screening, user, price, 1, 1),
            new Booking(screening, user, price, 2, 3),
            new Booking(screening, user, price, 5, 7)
        );

        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, null))
            .thenReturn(Optional.of(screening));

        var bookingResult = bookingService.book(user, movieName, roomName, screeningTime, seats);
        // Then
        Assertions.assertEquals(bookings, bookingResult);
    }

    @Test
    public void testBookWithInvalidSeatShouldThrowSeatDoesNotExistException() {
        // Given
        var user = new User("TestUser", "password", User.Role.USER);
        var price = 1500L;
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";
        var seats = "1,1 2,3 5,25";

        var movie = new Movie("Rambo", "anime", 150L);
        var room = new Room("Pedersoli", 10, 10);
        var screening = new Screening(movie, room, LocalDateTime.parse("2011-11-11 11:11", formatter));
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, null))
            .thenReturn(Optional.of(screening));
        // Then
        Assertions.assertThrows(SeatDoesNotExistException.class, () -> bookingService.book(user, movieName, roomName, screeningTime, seats));
    }

    @Test
    public void testBookWithTakenSeatShouldThrowSeatAlreadyTakenException() {
        // Given
        var user = new User("TestUser", "password", User.Role.USER);
        var price = 1500L;
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";
        var seats = "1,1 2,3 5,25";

        var movie = new Movie("Rambo", "anime", 150L);
        var room = new Room("Pedersoli", 10, 10);
        var screening = new Screening(movie, room, LocalDateTime.parse("2011-11-11 11:11", formatter));

        var bookings = Set.of(
            new Booking(screening, user, price, 1, 1),
            new Booking(screening, user, price, 2, 3),
            new Booking(screening, user, price, 5, 7)
        );

        screening.getBookings().addAll(bookings);
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, null))
            .thenReturn(Optional.of(screening));
        // Then
        Assertions.assertThrows(
            SeatAlreadyTakenException.class, () -> bookingService.book(user, movieName, roomName, screeningTime, seats));
    }

    @Test
    public void testCalculateOverallPriceForBookingShouldReturnCorrectPrice() {
        // Given
        var user = new User("TestUser", "password", User.Role.USER);
        var price = 1500L;

        var movie = new Movie("Rambo", "anime", 150L);
        var room = new Room("Pedersoli", 10, 10);
        var screening = new Screening(movie, room, LocalDateTime.parse("2011-11-11 11:11", formatter));

        var bookings = Set.of(
            new Booking(screening, user, price, 1, 1),
            new Booking(screening, user, price, 2, 3),
            new Booking(screening, user, price, 5, 7)
        );

        var expectedPrice = 4500L;
        // When
        var actualPrice = bookingService.calculateOverallPriceForBooking(bookings);
        // Then
        Assertions.assertEquals(expectedPrice, actualPrice);
    }

    @Test
    public void testShowPriceForBookingShouldReturnCorrectPrice() {
        // Given
        var price = 1500L;
        var movieName = "Rambo";
        var roomName = "Pedersoli";
        var screeningTime = "2011-11-11 11:11";
        var seats = "1,1 2,3 5,7";

        var movie = new Movie("Rambo", "anime", 150L);
        var room = new Room("Pedersoli", 10, 10);
        var screening = new Screening(movie, room, LocalDateTime.parse("2011-11-11 11:11", formatter));

        var expectedPrice = 4500L;
        // When
        Mockito.when(movieRepository.findByName(movieName)).thenReturn(Optional.of(movie));
        Mockito.when(roomRepository.findByName(roomName)).thenReturn(Optional.of(room));
        Mockito.when(screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room, null))
            .thenReturn(Optional.of(screening));
        Mockito.when(basePrice.getAmount()).thenReturn(price);

        var actualPrice = bookingService.showPriceForBooking(movieName, roomName, screeningTime, seats);
        // Then
        Assertions.assertEquals(expectedPrice, actualPrice);
    }
}
