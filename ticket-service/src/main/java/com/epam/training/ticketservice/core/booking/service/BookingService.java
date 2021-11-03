package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.user.model.User;

import java.util.Set;

public interface BookingService {
    Set<Booking> book(User user, String movieName, String roomName, String screeningTime, String seats);
}
