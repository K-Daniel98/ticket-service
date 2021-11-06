package com.epam.training.ticketservice.core.user.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class UserModelTest {

    @ParameterizedTest
    @ValueSource(strings = {"first_user","second_user_with_numbers123","123-234","--"})
    public void testEqualsUsersShouldBeEquivalentBasedOnTheirName(String username) {
        // Given
        var user = new User(username, "password", User.Role.USER);
        var otherUser = new User(username, "password123", User.Role.USER);
        // When
        var result = user.equals(otherUser);
        // Then
        Assertions.assertTrue(result);

    }

    @ParameterizedTest
    @CsvSource({"first_user,second_user", "random,asd", "this, "})
    public void testEqualsUsersShouldNotBeEquivalentBasedOnTheirName(String username, String otherUserName) {
        // Given
        var user = new User("first_user", "password", User.Role.USER);
        var otherUser = new User("second_user", "password123", User.Role.USER);
        // When
        var result = user.equals(otherUser);
        // Then
        Assertions.assertFalse(result);

    }

    @Test
    public void testEqualsSameObjectShouldReturnTrue() {
        // Given
        var user = new User("test_user", "password", User.Role.USER);
        var sameUser = user;
        // When
        var result = user.equals(sameUser);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testUserSetUsernameShouldSetValue() {
        // Given
        var user = new User("test_user", "password", User.Role.USER);
        var expected = "test_username2";
        // When
        user.setUsername("test_username2");
        // Then
        Assertions.assertEquals(expected, user.getUsername());
    }

    @Test
    public void testUserSetPasswordShouldSetValue() {
        // Given
        var user = new User("test_user", "password", User.Role.USER);
        var expected = "test_password2";
        // When
        user.setPassword("test_password2");
        // Then
        Assertions.assertEquals(expected, user.getPassword());
    }

    @Test
    public void testUserSetRoleShouldSetValue() {
        // Given
        var user = new User("test_user", "password", User.Role.USER);
        var expected = User.Role.ADMIN;
        // When
        user.setRole(User.Role.ADMIN);
        // Then
        Assertions.assertEquals(expected, user.getRole());
    }

    @Test
    public void testUserAddBookingsShouldAddNewBookingEntryToSetEvenWhenTheValueIsNull() {
        // Given
        var user = new User("test_user", "password", User.Role.USER);
        var expected = 1;
        // When
        user.getBookings().add(null);
        // Then
        Assertions.assertEquals(expected, user.getBookings().size());
    }

    @Test
    public void testEqualsWithNullObject() {
        // Given
        var user = new User("test_user", "password", User.Role.USER);
        User other = null;
        // When
        var result = user.equals(other);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testHashCodeShouldReturnTheSameValueBasedOnUsername() {
        // Given
        var user = new User("test_user", "password", User.Role.USER);
        var otherUser = new User("test_user", "password", User.Role.USER);
        // When
        var hashCode = user.hashCode();
        var otherHashCode = user.hashCode();
        // Then
        Assertions.assertEquals(hashCode, otherHashCode);
    }

}
