package com.epam.training.ticketservice.ui.command.impl;

import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.movie.service.MovieService;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.ui.command.AbstractUserStateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent
public class ScreeningCommand extends AbstractUserStateCommand {

    private final ScreeningService screeningService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final DateTimeFormatter dateTimeFormatter;

    @Autowired
    public ScreeningCommand(
        DateTimeFormatter dateTimeFormatter,
        ScreeningService screeningService,
        MovieService movieService,
        RoomService roomService,
        AuthService authService) {
        super(authService);
        this.dateTimeFormatter = dateTimeFormatter;
        this.screeningService = screeningService;
        this.movieService = movieService;
        this.roomService = roomService;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Creates a new screening", key = "create screening")
    public String createScreening(@ShellOption String movieName, @ShellOption String roomName,
                                  @ShellOption String screeningTime) throws IOException {

        try {
            LocalDateTime screeningDateTime = LocalDateTime.parse(screeningTime, dateTimeFormatter);

            var movie = movieService.getMovieByName(movieName);
            if (movie.isEmpty()) {
                return String.format("Movie '%s' does not exist!", movieName);
            }

            var room = roomService.getRoomByName(roomName);
            if (room.isEmpty()) {
                return String.format("Room '%s' does not exist!", roomName);
            }

            var screening = new Screening(movie.get(), room.get(), screeningDateTime);

            room.get()
                .getScreenings()
                .add(screening);

            screeningService.createScreening(screening);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }

        return "Screening has been created";
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Deletes a screening", key = "delete screening")
    public String deleteScreening(@ShellOption String movieName, @ShellOption String roomName,
                                  @ShellOption String screeningTime) {

        try {
            var screening = getScreening(movieName, roomName, screeningTime);

            if (screening.isEmpty()) {
                throw new RuntimeException("Screening does not exist");
            }

            screeningService.deleteScreening(screening.get());

        } catch (RuntimeException exception) {
            return exception.getMessage();
        }

        return "Screening has been deleted";
    }

    @ShellMethod(value = "Lists all screenings", key = "list screenings")
    public List<String> listScreenings() {
        var screenings = screeningService.listScreenings();

        if (screenings.size() == 0) {
            return List.of("There are no screenings");
        }

        return screenings
            .stream()
            .map(Screening::toString)
            .collect(Collectors.toList());
    }

    private Optional<Screening> getScreening(String movieName, String roomName, String screeningTime) {
        LocalDateTime screeningDateTime = LocalDateTime.parse(screeningTime, dateTimeFormatter);

        var movie = movieService.getMovieByName(movieName);
        if (movie.isEmpty()) {
            throw new RuntimeException(String.format("Movie '%s' does not exist", movieName));
        }

        var room = roomService.getRoomByName(roomName);
        if (room.isEmpty()) {
            throw new RuntimeException(String.format("Room '%s' does not exist", roomName));
        }

        return screeningService.getScreeningByMovieAndRoomAndScreeningTime(movie.get(), room.get(), screeningDateTime);
    }

}
