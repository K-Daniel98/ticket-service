package com.epam.training.ticketservice.core.booking.service.impl;

import com.epam.training.ticketservice.core.booking.exception.SeatAlreadyTakenException;
import com.epam.training.ticketservice.core.booking.exception.SeatDoesNotExistException;
import com.epam.training.ticketservice.core.booking.model.BookedSeat;
import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.booking.repository.BookingRepository;
import com.epam.training.ticketservice.core.booking.repository.SeatRepository;
import com.epam.training.ticketservice.core.booking.service.BookingService;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;
    private final ScreeningService screeningService;
    private final RoomService roomService;
    private final MovieService movieService;
    private final DateTimeFormatter formatter;

    private static final long basePrice = 1500;

    @Autowired
    public BookingServiceImpl(ScreeningRepository screeningRepository, BookingRepository bookingRepository,
                              ScreeningService screeningService, RoomService roomService, MovieService movieService,
                              DateTimeFormatter formatter, SeatRepository seatRepository) {
        this.screeningRepository = screeningRepository;
        this.bookingRepository = bookingRepository;
        this.screeningService = screeningService;
        this.roomService = roomService;
        this.movieService = movieService;
        this.formatter = formatter;
        this.seatRepository = seatRepository;
    }

    @Override
    public Set<Booking> book(User user, String movieName, String roomName, String screeningTime, String seats) {

        var movie = movieService.getMovieByName(movieName);

        if (movie.isEmpty()) {
            throw new MovieDoesNotExistException(movieName);
        }

        var room = roomService.getRoomByName(roomName);

        if (room.isEmpty()) {
            throw new RoomDoesNotExistException(roomName);
        }

        var plannedScreening = screeningService.getScreeningByMovieAndRoomAndScreeningTime(
            movie.get(),
            room.get(),
            LocalDateTime.parse(screeningTime, formatter)
        );

        if (plannedScreening.isEmpty()) {
            throw new ScreeningDoesNotExistException();
        }

        var screening = plannedScreening.get();

        var finalPrice = basePrice * seats.length();

        var splitted = seats.split(" ");

        for (var seatData : splitted) {

            var seatSplitted = seatData.split(",");

            var row = Integer.parseInt(seatSplitted[0]);
            var column = Integer.parseInt(seatSplitted[1]);

            var booking = new Booking(screening, user, row, column, finalPrice);

            if (!doesSeatExist(room.get(), row, column)) {
                throw new SeatDoesNotExistException(booking);
            }

            if (isSeatTaken(screening.getBookings(), row, column)) {
                throw new SeatAlreadyTakenException(booking);
            }

            user.getBookings().add(booking);
            screening.getBookings().add(booking);

        }

        screeningRepository.save(screening);

        return screening.getBookings();
    }

    private static boolean doesSeatExist(Room room, int row, int column) {
        if (row > room.getRows() || row < 0) {
            return false;
        }

        return column <= room.getColumns() && column >= 0;
    }

    private static boolean isSeatTaken(Set<Booking> bookings, int row, int column) {
        return bookings.stream()
            .anyMatch(booking -> booking.getRowNumber() == row && booking.getColumnNumber() == column);
    }
}
