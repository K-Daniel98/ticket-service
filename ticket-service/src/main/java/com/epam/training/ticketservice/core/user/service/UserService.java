package com.epam.training.ticketservice.core.user.service;

import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.user.model.User;

import java.util.Optional;

public interface UserService {

    void register(String username, String password);

    Optional<User> getUserByUsernameAndPassword(String username, String password);

    Optional<User> getUserByUsername(String username);

    void addBooking(User user, Booking booking);

}
