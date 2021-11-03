package com.epam.training.ticketservice.core.booking.service.impl;

import com.epam.training.ticketservice.core.booking.exception.SeatDoesNotExistException;
import com.epam.training.ticketservice.core.booking.model.BookedSeat;
import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.booking.repository.BookingRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ScreeningService screeningService;
    private final RoomService roomService;
    private final MovieService movieService;
    private final DateTimeFormatter formatter;

    private static final long basePrice = 1500;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ScreeningService screeningService, RoomService roomService, MovieService movieService, DateTimeFormatter formatter) {
        this.bookingRepository = bookingRepository;
        this.screeningService = screeningService;
        this.roomService = roomService;
        this.movieService = movieService;
        this.formatter = formatter;
    }

    @Override
    public Booking book(String movieName, String roomName, String screeningTime, String seats) {

        var movie = movieService.getMovieByName(movieName);

        if (movie.isEmpty()) {
            throw new MovieDoesNotExistException(movieName);
        }

        var room = roomService.getRoomByName(roomName);

        if (room.isEmpty()) {
            throw new RoomDoesNotExistException(roomName);
        }

        var screening = screeningService.getScreeningByMovieAndRoomAndScreeningTime(
            movie.get(),
            room.get(),
            LocalDateTime.parse(screeningTime, formatter)
        );

        if (screening.isEmpty()) {
            throw new ScreeningDoesNotExistException();
        }

        var bookedSeats = new HashSet<BookedSeat>();

        var splitted = seats.split(" ");

        for (var seatData : splitted) {

            var seatSplitted = seatData.split(",");

            var seat = new BookedSeat(
                Integer.parseInt(seatSplitted[0]),
                Integer.parseInt(seatSplitted[1])
            );

            if (!doesSeatExist(room.get(), seat.getRowNumber(), seat.getColumnNumber())) {
                throw new SeatDoesNotExistException(seat);
            }

            if (bookingRepository.existsBookingBySeats(seat) || !bookedSeats.add(seat)) {
                throw new SeatDoesNotExistException(seat);
            }

        }

        var finalPrice = basePrice * seats.length();

        var booking = new Booking(screening.get(), bookedSeats, finalPrice);

        bookingRepository.save(booking);

        return booking;
    }

    private static boolean doesSeatExist(Room room, int row, int column) {
        if (row > room.getRows() || row < 0) {
            return false;
        }

        return column <= room.getColumns() && column >= 0;
    }
}
