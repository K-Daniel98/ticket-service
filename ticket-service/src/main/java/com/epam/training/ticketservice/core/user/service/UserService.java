package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.user.model.User;

import java.util.Optional;

public interface UserService {

    void register(User user);

    Optional<User> getUserByUsernameAndPassword(String username, String password);

    Optional<User> getUserByUsername(String username);

}
