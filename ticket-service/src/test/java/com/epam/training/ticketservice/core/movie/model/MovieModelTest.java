package com.epam.training.ticketservice.core.movie.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MovieModelTest {

    @Test
    public void testEqualsBasedOnMovieNameShouldReturnTrue() {
        // Given
        var movie = new Movie("Rambo", "action", 190L);
        var otherMovie = new Movie("Rambo", "action", 190L);
        // When
        var result = movie.equals(otherMovie);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsBasedOnMovieNameShouldReturnFalse() {
        // Given
        var movie = new Movie("Rambo", "action", 190L);
        var otherMovie = new Movie("Rambo 2", "action", 190L);
        // When
        var result = movie.equals(otherMovie);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentTypesShouldReturnFalse() {
        // Given
        var movie = new Movie("Rambo", "action", 190L);
        var otherType = "a string";
        // When
        var result = movie.equals(otherType);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsSameObjectShouldReturnTrue() {
        // Given
        var movie = new Movie("Rambo", "action", 190L);
        var otherMovie = movie;
        // When
        var result = movie.equals(otherMovie);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        // Given
        var movie = new Movie("Rambo", "action", 190L);
        Movie otherMovie = null;
        // When
        var result = movie.equals(otherMovie);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testHashCodeShouldReturnTheSameValueBasedOnMovieNameAndTypeAndLength() {
        // Given
        var movie = new Movie("Rambo", "action", 190L);
        var otherMovie = new Movie("Rambo", "action", 190L);
        // When
        var hashCode = movie.hashCode();
        var otherHashCode = otherMovie.hashCode();
        // Then
        Assertions.assertEquals(hashCode, otherHashCode);
    }

    @Test
    public void testToStringShouldReturnProperString() {
        // Given
        var movie = new Movie("Rambo", "action", 190L);
        var expected = String.format("%s (%s, %d minutes)", movie.getName(), movie.getType(), movie.getLength());
        // When
        var str = movie.toString();
        // Then
        Assertions.assertEquals(expected, str);
    }

}
