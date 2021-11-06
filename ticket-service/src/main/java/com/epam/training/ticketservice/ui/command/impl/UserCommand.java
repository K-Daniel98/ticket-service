package com.epam.training.ticketservice.ui.command.impl;

import com.epam.training.ticketservice.core.booking.model.Booking;
import com.epam.training.ticketservice.core.booking.service.BookingService;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.user.model.User;
import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.user.service.UserService;
import com.epam.training.ticketservice.core.utils.formatter.DateTimeFormatterUtil;
import com.epam.training.ticketservice.ui.command.AbstractUserStateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ShellComponent
public class UserCommand extends AbstractUserStateCommand {

    private final UserService userService;
    private final BookingService bookingService;
    private final DateTimeFormatterUtil dateTimeFormatterUtil;

    @Autowired
    public UserCommand(
        AuthService authService,
        UserService userService,
        BookingService bookingService,
        DateTimeFormatterUtil dateTimeFormatterUtil) {
        super(authService);
        this.userService = userService;
        this.bookingService = bookingService;
        this.dateTimeFormatterUtil = dateTimeFormatterUtil;
    }

    @ShellMethod(value = "Sign in as an administrator", key = "sign in privileged")
    public String signInPrivileged(@ShellOption String username, @ShellOption String password) {
        return signIn(username, password);
    }

    @ShellMethod(value = "Sign in as a user", key = "sign in")
    public String signInRegular(@ShellOption String username, @ShellOption String password) {
        return signIn(username, password);
    }

    private String signIn(String username, String password) {
        try {
            authService.login(username, password);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return null;
    }

    @ShellMethod(value = "Sign out", key = "sign out")
    public void signOut() {
        authService.logout();
    }

    @ShellMethod(value = "Describe account", key = "describe account")
    public List<String> describeAccount() {
        if (authService.getLoggedInUser().isEmpty()) {
            return List.of("You are not signed in");
        }

        var user = authService.getLoggedInUser().get();

        if (user.getRole() == User.Role.ADMIN) {
            return List.of(String.format("Signed in with privileged account '%s'", user.getUsername()));
        }

        var result =
            new java.util.ArrayList<>(List.of(String.format("Signed in with account '%s'", user.getUsername())));

        var bookingData = getUniqueBookings(user.getBookings()).entrySet()
            .stream()
            .map(kvp -> getBookingDataString(kvp.getValue(), kvp.getKey()))
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
    public String signUp(@ShellOption String username, @ShellOption String password) {
        try {
            userService.register(username, password);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return null;
    }

    @ShellMethodAvailability("notAdmin")
    @ShellMethod(value = "Book a ticket", key = "book")
    public String bookTicket(@ShellOption String movieName, @ShellOption String roomName,
                              @ShellOption String screeningDate, @ShellOption String listOfSeats) {
        try {
            var user = authService.getLoggedInUser().get();
            var bookings = bookingService.book(user, movieName, roomName, screeningDate, listOfSeats);

            var seatStr = bookings.stream()
                .map(Booking::toString)
                .collect(Collectors.joining(", "));

            var finalPrice = bookingService.calculateOverallPriceForBooking(bookings);

            return String.format("Seats booked: %s; the price of this booking is %d HUF", seatStr, finalPrice);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
    }

    @ShellMethod(value = "Display the discounted (if any) price for a given screening", key = "show price for")
    public String showPrice(@ShellOption String movieName,
                            @ShellOption String roomName,
                            @ShellOption String screeningTime,
                            @ShellOption String listOfSeats) {
        try {
            var price = bookingService.showPriceForBooking(movieName, roomName, screeningTime, listOfSeats);
            return String.format("The price for this booking would be %d HUF", price);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
    }

    private static Map<Screening, List<Booking>> getUniqueBookings(Set<Booking> bookingSet) {
        var bookings = new HashMap<Screening, List<Booking>>();

        for (var booking : bookingSet) {
            var screening = booking.getScreening();
            if (bookings.containsKey(screening)) {
                bookings.get(screening).add(booking);
            } else {
                bookings.put(screening, new ArrayList<>(List.of(booking)));
            }
        }

        return bookings;
    }

    private List<String> getBookingData(List<List<Booking>> bookings, Screening screening) {
        return bookings.stream()
            .map(bookingList -> getBookingDataString(bookingList, screening))
            .collect(Collectors.toList());
    }

    private String getBookingDataString(List<Booking> bookings, Screening screening) {

        var seats = bookings.stream()
            .map(Booking::toString)
            .collect(Collectors.joining(", "));

        var finalPrice = bookings.stream()
            .mapToLong(Booking::getPrice)
            .reduce(Long::sum)
            .orElseThrow(() -> new RuntimeException("Final price was not present"));

        return String.format("Seat %s on %s in room %s starting at %s for %d HUF",
            seats,
            screening.getMovie(),
            screening.getRoom().getName(),
            dateTimeFormatterUtil.fromLocalDateTime(screening.getScreeningTime()),
            finalPrice);
    }

}
