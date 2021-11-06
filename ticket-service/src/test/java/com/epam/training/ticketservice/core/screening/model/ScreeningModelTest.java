package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.pricing.model.PriceComponent;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.user.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ScreeningModelTest {

    private final Room room;
    private final Movie movie;
    private final LocalDateTime screeningTime = LocalDateTime.now();

    public ScreeningModelTest() {
        room = new Room("Pedersoli", 10, 12);
        movie = new Movie("Test movie", "action", 150L);
    }

    @Test
    public void testEqualsBasedOnMovieAndRoomAndMovieAndScreeningTimeShouldReturnTrue() {
        // Given
        var screening = new Screening(movie, room, screeningTime);
        var otherScreening = new Screening(movie, room, screeningTime);
        // When
        var result = screening.equals(otherScreening);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsBasedOnMovieAndRoomAndMovieAndScreeningTimeShouldReturnFalse() {
        // Given
        var screening = new Screening(movie, room, screeningTime);
        var otherScreening = new Screening(movie, room, LocalDateTime.now().minusHours(1));
        // When
        var result = screening.equals(otherScreening);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentTypesShouldReturnFalse() {
        // Given
        var screening = new Screening(movie, room, screeningTime);
        var otherType = "a string";
        // When
        var result = screening.equals(otherType);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsSameObjectShouldReturnTrue() {
        // Given
        var screening = new Screening(movie, room, screeningTime);
        var otherScreening = screening;
        // When
        var result = screening.equals(otherScreening);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        // Given
        var screening = new Screening(movie, room, screeningTime);
        Screening otherScreening = null;
        // When
        var result = screening.equals(otherScreening);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testHashCodeShouldReturnTheSameValueBasedOnRoomAndMovieAndScreeningTime() {
        // Given
        var screening = new Screening(movie, room, screeningTime);
        var other = new Screening(movie, room, screeningTime);
        // When
        var hashCode = screening.hashCode();
        var otherHashCode = other.hashCode();
        // Then
        Assertions.assertEquals(hashCode, otherHashCode);
    }

    @Test
    public void testSetMovieShouldSetValue() {
        // Given
        var newMovie = new Movie("Rambo v2", "action", 180L);
        var screening = new Screening(movie, room, screeningTime);
        // When
        screening.setMovie(newMovie);
        // Then
        Assertions.assertEquals(newMovie, screening.getMovie());
    }

    @Test
    public void testSetRoomShouldSetValue() {
        // Given
        var newRoom = new Room("Pedersoli v3", 10, 5);
        var screening = new Screening(movie, room, screeningTime);
        // When
        screening.setRoom(newRoom);
        // Then
        Assertions.assertEquals(newRoom, screening.getRoom());
    }

    @Test
    public void testSetPriceComponentShouldSetValue() {
        // Given
        var priceComponent = new PriceComponent("Price component", -150L);
        var screening = new Screening(movie, room, screeningTime);
        // When
        screening.setPriceComponent(priceComponent);
        // Then
        Assertions.assertEquals(priceComponent, screening.getPriceComponent());
    }

    @Test
    public void testSetScreeningTimeShouldSetValue() {
        // Given
        var newScreeningTime = LocalDateTime.now();
        var screening = new Screening(movie, room, screeningTime);
        // When
        screening.setScreeningTime(newScreeningTime);
        // Then
        Assertions.assertEquals(newScreeningTime, screening.getScreeningTime());
    }

    @Test
    public void testGetBookingsShouldReturnNonEmptySet() {
        // Given
        var screening = new Screening(movie, room, screeningTime);
        var user = new User("test", "test", User.Role.USER);

        var booking = new Booking(screening, user, 100L, 10, 10);
        // When
        screening.getBookings().add(booking);
        // Then
        Assertions.assertFalse(screening.getBookings().isEmpty());
    }

}
