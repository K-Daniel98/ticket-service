package com.epam.training.ticketservice.core.booking.repository;

import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.screening.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingsByScreening(Screening screening);
}
