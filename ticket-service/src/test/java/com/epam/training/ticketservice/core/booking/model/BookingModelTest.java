package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class BookingModelTest {

    private final Room room;
    private final Movie movie;
    private final LocalDateTime screeningTime;
    private final User user;
    private final Screening screening;

    public BookingModelTest() {
        room = new Room("test", 10, 10);
        movie = new Movie("Rambo", "action", 120L);
        screeningTime = LocalDateTime.now();
        user = new User("test_user", "<empty>", User.Role.USER);
        screening = new Screening(movie, room, screeningTime);
    }

    @Test
    public void testEqualsBasedOnRowAndColumnAndScreeningShouldReturnTrue() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var booking = new Booking(screening, user, price, row, column);
        var otherBooking = new Booking(screening, user, price, row, column);
        // When
        var result = booking.equals(otherBooking);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsBasedOnRowAndColumnAndScreeningShouldReturnFalse() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var booking = new Booking(screening, user, price, row, column);
        var otherBooking = new Booking(screening, user, price, row + 1, column + 2);
        // When
        var result = booking.equals(otherBooking);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentTypesShouldReturnFalse() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var booking = new Booking(screening, user, price, row, column);
        var otherBooking = "str";
        // When
        var result = booking.equals(otherBooking);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsSameObjectShouldReturnTrue() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var booking = new Booking(screening, user, price, row, column);
        var otherBooking = booking;
        // When
        var result = booking.equals(otherBooking);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var booking = new Booking(screening, user, price, row, column);
        Booking otherBooking = null;
        // When
        var result = booking.equals(otherBooking);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testHashCodeShouldReturnTheSameValueBasedOnScreeningAndRowAndColumn() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var booking = new Booking(screening, user, price, row, column);
        var otherBooking = new Booking(screening, user, price, row, column);
        // When
        var hashCode = booking.hashCode();
        var otherHashCode = otherBooking.hashCode();
        // Then
        Assertions.assertEquals(hashCode, otherHashCode);
    }

    @Test
    public void testToStringShouldReturnProperString() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var booking = new Booking(screening, user, price, row, column);
        var expected = String.format("(%d, %d)", row, column);
        // When
        var resultString = booking.toString();
        // Then
        Assertions.assertEquals(expected, resultString);
    }

    @Test
    public void testSetScreeningShouldSetValue() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var newRoom = new Room("Pedersoli", 2, 2);
        var newScreening = new Screening(movie, newRoom, screeningTime);

        var booking = new Booking(screening, user, price, row, column);
        // When
        booking.setScreening(newScreening);
        // Then
        Assertions.assertEquals(newScreening, booking.getScreening());
    }

    @Test
    public void testSetUserShouldSetValue() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var newUser = new User("other user", "password", User.Role.USER);
        var booking = new Booking(screening, user, price, row, column);
        // When
        booking.setUser(newUser);
        // Then
        Assertions.assertEquals(newUser, booking.getUser());
    }

    @Test
    public void testSetPriceShouldSetValue() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var newPrice = 1900L;
        var booking = new Booking(screening, user, price, row, column);
        // When
        booking.setPrice(newPrice);
        // Then
        Assertions.assertEquals(newPrice, booking.getPrice());
    }

    @Test
    public void testSetRowShouldSetValue() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var newRow = 7;
        var booking = new Booking(screening, user, price, row, column);
        // When
        booking.setRowNumber(newRow);
        // Then
        Assertions.assertEquals(newRow, booking.getRowNumber());
    }

    @Test
    public void testSetColumnShouldSetValue() {
        // Given
        var row = 5;
        var column = 2;
        var price = 1500L;

        var newColumn = 2;
        var booking = new Booking(screening, user, price, row, column);
        // When
        booking.setColumnNumber(newColumn);
        // Then
        Assertions.assertEquals(newColumn, booking.getColumnNumber());
    }

}
