package com.epam.training.ticketservice.ui.command.impl;

import com.epam.training.ticketservice.configuration.ApplicationConfiguration;
import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.booking.service.BookingService;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.ui.command.AbstractUserStateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class UserCommand extends AbstractUserStateCommand {

    private final UserService userService;
    private final BookingService bookingService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final ScreeningService screeningService;
    private final DateTimeFormatter formatter;

    @Autowired
    public UserCommand(
        AuthService authService,
        UserService userService,
        BookingService bookingService,
        MovieService movieService,
        RoomService roomService,
        ScreeningService screeningService,
        DateTimeFormatter formatter) {
        super(authService);
        this.userService = userService;
        this.bookingService = bookingService;
        this.movieService = movieService;
        this.roomService = roomService;
        this.screeningService = screeningService;
        this.formatter = formatter;
    }

    @ShellMethod(value = "Sign in as an administrator", key = "sign in privileged")
    private String signInPrivileged(@ShellOption String username, @ShellOption String password) {
        return signIn(username, password);
    }

    @ShellMethod(value = "Sign in as a user", key = "sign in")
    private String signInRegular(@ShellOption String username, @ShellOption String password) {
        return signIn(username, password);
    }

    private String signIn(String username, String password) {
        try {
            authService.login(username, password);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }

        return "Successful login";
    }

    @ShellMethod(value = "Sign out", key = "sign out")
    private String signOut() {
        authService.logout();
        return "Signed out";
    }

    @ShellMethod(value = "Describe account", key = "describe account")
    private List<String> describeAccount() {
        if (authService.getLoggedInUser().isEmpty()) {
            return List.of("You are not signed in");
        }

        var user = authService.getLoggedInUser().get();

        if (user.getRole() == User.Role.ADMIN) {
            return List.of(String.format("Signed in with privileged account '%s'", user.getUsername()));
        }

        var result =
            new java.util.ArrayList<>(List.of(String.format("Signed in with account '%s'", user.getUsername())));

        var bookingData = user.getBookings().stream()
            .map(UserCommand::getBookingDataString)
            .collect(Collectors.toList());

        if (bookingData.size() == 0) {
            result.add("You have not booked any tickets yet");
        } else {
            result.add("Your previous bookings are");
        }

        result.addAll(bookingData);

        return result;
    }

    @ShellMethod(value = "Sign up", key = "sign up")
    private String signUp(@ShellOption String username, @ShellOption String password) {
        var user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            return String.format("A user with name '%s' already exists", username);
        }

        userService.register(new User(username, password, User.Role.USER));

        return "Account created";
    }

    @ShellMethodAvailability("notAdmin")
    @ShellMethod(value = "Book a ticket", key = "book")
    private String bookTicket(@ShellOption String movieName, @ShellOption String roomName,
                              @ShellOption String screeningDate, @ShellOption String listOfSeats) {
        try {

            var user = authService.getLoggedInUser().get();
            var bookings = bookingService.book(user, movieName, roomName, screeningDate, listOfSeats);

            var seatStr = bookings.stream()
                .map(Booking::toString)
                .collect(Collectors.joining(", "));

            var finalPrice = bookings.stream()
                .mapToLong(Booking::getPrice)
                .reduce(Long::sum)
                .getAsLong();

            return String.format("Seats booked: %s; the price of this booking is %d HUF", seatStr, finalPrice);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
    }

    private static String getBookingDataString(Booking booking) {
        var screening = booking.getScreening();

        return String.format("Seat %s on %s in room %s starting at %s for %d HUF",
            booking,
            screening.getMovie(),
            screening.getRoom(),
            screening.getScreeningTime().format(ApplicationConfiguration.formatter),
            booking.getPrice());
    }

}
