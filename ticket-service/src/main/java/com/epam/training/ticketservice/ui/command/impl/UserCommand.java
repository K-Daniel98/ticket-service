package com.epam.training.ticketservice.ui.command.impl;

import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.booking.model.BookedSeat;
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
    private String describeAccount() {
        if (authService.getLoggedInUser().isEmpty()) {
            return "You are not signed in";
        }

        var user = authService.getLoggedInUser().get();

        if (user.getRole() == User.Role.ADMIN) {
            return String.format("Signed in with privileged account '%s'", user.getUsername());
        }

        return String.format("Signed in with account '%s'", user.getUsername());
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

            var booked = bookingService.book(movieName, roomName, screeningDate, listOfSeats);

            var seatsStr = booked.getSeats().stream().map(BookedSeat::toString).collect(Collectors.joining(", "));

            return String.format("Seats booked: %s; the price of this booking is %d HUF", seatsStr, booked.getPrice());

        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
    }

}
