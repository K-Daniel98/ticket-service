package com.epam.training.ticketservice.core.booking.service;

import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.screening.model.Screening;

public interface BookingService {
    Booking book(String movieName, String roomName, String screeningTime, String seats);
}
