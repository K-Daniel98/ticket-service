package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.exception.UserAlreadyExistsException;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.repository.UserRepository;
import com.epam.training.ticketservice.core.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testRegisterShouldThrowUserAlreadyExistsException() {
        // Given
        var username = "first_user";
        var password = "password";
        // When
        Mockito.when(userRepository.existsById(username)).thenReturn(true);
        // Then
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> userService.register(username, password));
    }

    @Test
    public void testRegisterShouldSucceed() {
        // Given
        var username = "first_user";
        var password = "password";
        // When
        Mockito.when(userRepository.existsById(username)).thenReturn(false);
        // Then
        Assertions.assertDoesNotThrow(() -> userService.register(username, password));
    }

    @Test
    public void testGetUserByUsernameAndPasswordShouldReturnUser() {
        // Given
        var username = "first_user";
        var password = "password";

        var user = new User(username, password, User.Role.USER);
        // When
        Mockito.when(userRepository.findByUsernameAndPassword(username, password)).thenReturn(Optional.of(user));
        // Then
        Assertions.assertEquals(user, userService.getUserByUsernameAndPassword(username, password).get());
    }

    @Test
    public void testGetUserByUsernameShouldReturnUser() {
        // Given
        var username = "first_user";
        var password = "password";

        var user = new User(username, password, User.Role.USER);
        // When
        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        // Then
        Assertions.assertEquals(user, userService.getUserByUsername(username).get());
    }

}
