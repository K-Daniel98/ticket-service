package com.epam.training.ticketservice.core.booking.service.impl;

import com.epam.training.ticketservice.core.booking.exception.SeatAlreadyTakenException;
import com.epam.training.ticketservice.core.booking.exception.SeatDoesNotExistException;
import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.booking.service.BookingService;
import com.epam.training.ticketservice.core.movie.exception.MovieDoesNotExistException;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.pricing.model.BasePrice;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.exception.ScreeningDoesNotExistException;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Set;

@Service
public class BookingServiceImpl implements BookingService {

    private final ScreeningRepository screeningRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final UserService userService;
    private final DateTimeFormatter formatter;
    private final BasePrice basePrice;

    @Autowired
    public BookingServiceImpl(ScreeningRepository screeningRepository,
                              RoomRepository roomRepository,
                              MovieRepository movieRepository,
                              UserService userService,
                              DateTimeFormatter formatter,
                              BasePrice basePrice) {
        this.screeningRepository = screeningRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.userService = userService;
        this.formatter = formatter;
        this.basePrice = basePrice;
    }

    @Override
    public Set<Booking> book(User user, String movieName, String roomName, String screeningTime, String seats) {

        var movie = movieRepository.findByName(movieName)
            .orElseThrow(() -> new MovieDoesNotExistException(movieName));

        var room = roomRepository.findByName(roomName)
            .orElseThrow(() -> new RoomDoesNotExistException(roomName));

        var plannedScreening = screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room,
                LocalDateTime.parse(screeningTime, formatter))
            .orElseThrow(ScreeningDoesNotExistException::new);

        var splitted = seats.split(" ");

        var pricePerBooking = calculateFinalPrice(movie, room, plannedScreening);

        for (var seatData : splitted) {

            var seatSplitted = seatData.split(",");

            var row = Integer.parseInt(seatSplitted[0]);
            var column = Integer.parseInt(seatSplitted[1]);

            var booking = new Booking(plannedScreening, user, pricePerBooking, row, column);

            if (!doesSeatExist(room, row, column)) {
                throw new SeatDoesNotExistException(booking);
            }

            if (isSeatTaken(plannedScreening.getBookings(), row, column)) {
                throw new SeatAlreadyTakenException(booking);
            }

            userService.addBooking(user, booking);
            plannedScreening.getBookings().add(booking);

        }

        screeningRepository.save(plannedScreening);

        return plannedScreening.getBookings();
    }

    @Override
    public long showPriceForBooking(String movieName, String roomName, String screeningTime, String seats) {

        var movie = movieRepository.findByName(movieName)
            .orElseThrow(() -> new MovieDoesNotExistException(movieName));

        var room = roomRepository.findByName(roomName)
            .orElseThrow(() -> new RoomDoesNotExistException(roomName));

        var plannedScreening = screeningRepository.findScreeningByMovieAndRoomAndScreeningTime(movie, room,
                LocalDateTime.parse(screeningTime, formatter))
            .orElseThrow(ScreeningDoesNotExistException::new);

        var numberOfSeats = seats.split(" ").length;
        return numberOfSeats * calculateFinalPrice(movie, room, plannedScreening);
    }

    @Override
    public long calculateOverallPriceForBooking(Collection<Booking> bookings) {
        return bookings.stream()
            .mapToLong(Booking::getPrice)
            .reduce(Long::sum)
            .orElseThrow(() -> new RuntimeException("Final price was not present"));
    }

    private long calculateFinalPrice(Movie movie, Room room, Screening screening) {
        var priceComponentSum = 0L;

        if (movie.getPriceComponent() != null) {
            priceComponentSum += movie.getPriceComponent().getAmount();
        }

        if (room.getPriceComponent() != null) {
            priceComponentSum += room.getPriceComponent().getAmount();
        }

        if (screening.getPriceComponent() != null) {
            priceComponentSum += screening.getPriceComponent().getAmount();
        }

        return basePrice.getAmount() + priceComponentSum;
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
