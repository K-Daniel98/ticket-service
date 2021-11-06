package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.exception.InvalidCredentialsException;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    public void testLoginShouldThrowInvalidCredentialsException() {
        // Given
        var username = "first_user";
        var password = "password";
        // When
        Mockito.when(userService.getUserByUsernameAndPassword(username, password)).thenReturn(Optional.empty());
        // Then
        Assertions.assertThrows(InvalidCredentialsException.class, () -> authService.login(username, password));
    }

    @Test
    public void testLoginShouldSucceed() {
        // Given
        var username = "first_user";
        var password = "password";

        var user = new User(username, password, User.Role.USER);
        // When
        Mockito.when(userService.getUserByUsernameAndPassword(username, password)).thenReturn(Optional.of(user));
        // Then
        Assertions.assertDoesNotThrow(() -> authService.login(username, password));
    }

    @Test
    public void testLogout() {
        // When
        authService.logout();
        // Then
        Assertions.assertTrue(authService.getLoggedInUser().isEmpty());
    }

}
