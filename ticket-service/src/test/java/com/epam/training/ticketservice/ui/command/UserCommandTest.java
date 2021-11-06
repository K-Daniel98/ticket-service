package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.booking.service.BookingService;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.user.exception.InvalidCredentialsException;
import com.epam.training.ticketservice.core.user.exception.UserAlreadyExistsException;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.utils.formatter.DateTimeFormatterUtil;
import com.epam.training.ticketservice.ui.command.impl.UserCommand;
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
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class UserCommandTest {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Mock
    private BookingService bookingService;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    DateTimeFormatterUtil dateTimeFormatterUtil;

    @InjectMocks
    private UserCommand userCommand;

    @Test
    public void testDescribeAccountShouldReturnNotLoggedInText() {
        // Given
        var expectedOutput = List.of("You are not signed in");
        // When
        Mockito.when(authService.getLoggedInUser())
            .thenReturn(Optional.empty());
        var actualOutput = userCommand.describeAccount();
        // Then
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDescribeAccountWithPrivilegedAccount() {
        // Given
        var username = "test_user";
        var password = "password";

        var user = new User(username, password, User.Role.ADMIN);
        var expectedOutput = List.of(String.format("Signed in with privileged account '%s'", user.getUsername()));
        // When
        Mockito.when(authService.getLoggedInUser())
            .thenReturn(Optional.of(user));
        var actualOutput = userCommand.describeAccount();
        // Then
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDescribeAccountWithRegularAccountWithoutBookingsShouldReturnData() {
        // Given
        var username = "test_user";
        var password = "password";

        var user = new User(username, password, User.Role.USER);

        var expectedOutput = List.of(
            String.format("Signed in with account '%s'", user.getUsername()),
            "You have not booked any tickets yet"
        );
        // When
        Mockito.when(authService.getLoggedInUser())
            .thenReturn(Optional.of(user));

        var actualOutput = userCommand.describeAccount();
        // Then
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testDescribeAccountWithRegularAccountWithBookingsShouldReturnData() {
        // Given
        var username = "test_user";
        var password = "password";

        var user = new User(username, password, User.Role.USER);

        var movieName = "Rambo";
        var movieType = "anime";
        var movieLength = 120L;

        var movie = new Movie(movieName, movieType, movieLength);

        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;

        var room = new Room(roomName, rows, cols);

        var screeningTimeStr = "2011-11-11 11:11";
        var screeningTime = LocalDateTime.parse(screeningTimeStr, formatter);

        var screening = new Screening(movie, room, screeningTime);

        var bookingPrice = 1500L;
        var bookingRow = 2;
        var bookingColumn = 1;

        var booking = new Booking(screening, user, bookingPrice, bookingRow, bookingColumn);

        user.getBookings().add(booking);

        var expectedOutputFirstLine = String.format("Signed in with account '%s'", user.getUsername());
        var expectedLength = 3;
        // When
        Mockito.when(authService.getLoggedInUser())
            .thenReturn(Optional.of(user));
        Mockito.when(dateTimeFormatterUtil.fromLocalDateTime(screeningTime)).thenReturn(screeningTimeStr);

        var actualOutput = userCommand.describeAccount();
        // Then
        Assertions.assertEquals(expectedOutputFirstLine, actualOutput.get(0));
        Assertions.assertEquals(expectedLength, actualOutput.size());
    }

    @Test
    public void testSignUpShouldThrowUserAlreadyExistException() {
        // Given
        var username = "first_user";
        var password = "password";

        var exception = new UserAlreadyExistsException(username);
        var expectedOutput = exception.getMessage();
        // When
        Mockito.doThrow(exception).when(userService).register(username, password);

        var actualOutput = userCommand.signUp(username, password);
        // Then
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSignUpShouldSucceed() {
        // Given
        var username = "first_user";
        var password = "password";
        // When
        Mockito.doNothing().when(userService).register(username, password);
        var actualOutput = userCommand.signUp(username, password);
        // Then
        Assertions.assertNull(actualOutput);
    }

    @Test
    public void testSignInPrivilegedShouldThrowInvalidCredentialsException() {
        // Given
        var username = "first_user";
        var password = "password";

        var exception = new InvalidCredentialsException();
        var expectedOutput = exception.getMessage();
        // When
        Mockito.doThrow(exception).when(authService).login(username, password);
        var actualOutput = userCommand.signInPrivileged(username, password);
        // Then
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSignInPrivilegedShouldSucceed() {
        // Given
        var username = "first_user";
        var password = "password";
        // When
        Mockito.doNothing().when(authService).login(username, password);
        var actualOutput = userCommand.signInPrivileged(username, password);
        // Then
        Assertions.assertNull(actualOutput);
    }

    @Test
    public void testSignInRegularShouldThrowInvalidCredentialsException() {
        // Given
        var username = "first_user";
        var password = "password";

        var exception = new InvalidCredentialsException();
        var expectedOutput = exception.getMessage();
        // When
        Mockito.doThrow(exception).when(authService).login(username, password);
        var actualOutput = userCommand.signInRegular(username, password);
        // Then
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testSignInRegularShouldSucceed() {
        // Given
        var username = "first_user";
        var password = "password";
        // When
        Mockito.doNothing().when(authService).login(username, password);
        var actualOutput = userCommand.signInRegular(username, password);
        // Then
        Assertions.assertNull(actualOutput);
    }

    @Test
    public void testShowPriceShouldReturnPriceForBooking() {
        // Given
        var movieName = "Rambo";

        var roomName = "Pedersoli";

        var screeningTimeStr = "2011-11-11 11:11";

        var listOfSeats = "1,2, 2,5";

        var expectedPrice = 3000L;
        var expectedOutput = String.format("The price for this booking would be %d HUF", expectedPrice);
        // When
        Mockito.when(bookingService.showPriceForBooking(movieName, roomName, screeningTimeStr,listOfSeats))
            .thenReturn(expectedPrice);

        var actualOutput = userCommand.showPrice(movieName, roomName, screeningTimeStr, listOfSeats);
        // Then
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testBookTicketShouldSucceed() {
        // Given
        var username = "first_user";
        var password = "password";

        var user = new User(username, password, User.Role.USER);

        var movieName = "Rambo";
        var movieType = "anime";
        var movieLength = 120L;

        var movie = new Movie(movieName, movieType, movieLength);

        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;

        var room = new Room(roomName, rows, cols);

        var screeningTimeStr = "2011-11-11 11:11";
        var screeningTime = LocalDateTime.parse(screeningTimeStr, formatter);

        var screening = new Screening(movie, room, screeningTime);

        var listOfSeatsStr = "1,2 3,4";

        var pricePerBooking = 1500L;
        var finalPrice = 2 * pricePerBooking;

        var bookings = Set.of(
            new Booking(screening, user, pricePerBooking, 1, 2),
            new Booking(screening, user, pricePerBooking, 3, 4)
        );

        var seatStr = bookings.stream()
            .map(Booking::toString)
            .collect(Collectors.joining(", "));

        var expectedOutput = String.format("Seats booked: %s; the price of this booking is %d HUF", seatStr, finalPrice);;
        // When
        Mockito.when(authService.getLoggedInUser())
            .thenReturn(Optional.of(user));
        Mockito.when(bookingService.book(user, movieName, roomName, screeningTimeStr, listOfSeatsStr))
            .thenReturn(bookings);
        Mockito.when(bookingService.calculateOverallPriceForBooking(bookings))
            .thenReturn(finalPrice);

        var actualOutput = userCommand.bookTicket(movieName, roomName, screeningTimeStr, listOfSeatsStr);
        // Then
        Assertions.assertEquals(expectedOutput, actualOutput);
    }

}
