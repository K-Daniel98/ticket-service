package com.epam.training.ticketservice.core.booking.repository;

import com.epam.training.ticketservice.core.booking.model.BookedSeat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<BookedSeat, Long> {
}
