package com.epam.training.ticketservice.core.user.service.impl;

import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.repository.UserRepository;
import com.epam.training.ticketservice.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByUsernameAndPassword(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void addBooking(User user, Booking booking) {
        user.getBookings().add(booking);
        userRepository.save(user);
    }
}
