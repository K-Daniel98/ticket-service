package com.epam.training.ticketservice.core.booking.repository;

import com.epam.training.ticketservice.core.booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
