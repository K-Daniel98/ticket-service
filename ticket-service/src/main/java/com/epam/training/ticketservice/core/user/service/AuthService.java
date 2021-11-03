package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.model.User;

import java.util.Optional;

public interface AuthService {

    void login(String username, String password);

    void logout();

    Optional<User> getLoggedInUser();

}
