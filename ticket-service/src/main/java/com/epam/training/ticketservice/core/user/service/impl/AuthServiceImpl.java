package com.epam.training.ticketservice.core.user.service.impl;

import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private User user;

    @Autowired
    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void login(String username, String password) {
        user = userService.getUserByUsernameAndPassword(username, password)
            .orElseThrow(() -> new RuntimeException("Login failed due to incorrect credentials"));
    }

    @Override
    public void logout() {
        user = null;
    }

    @Override
    public Optional<User> getLoggedInUser() {
        return Optional.ofNullable(user);
    }
}
