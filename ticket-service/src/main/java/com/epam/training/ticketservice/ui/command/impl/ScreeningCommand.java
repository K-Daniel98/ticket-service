package com.epam.training.ticketservice.ui.command.impl;

import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.screening.service.ScreeningService;
import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.ui.command.AbstractUserStateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class ScreeningCommand extends AbstractUserStateCommand {

    private final ScreeningService screeningService;
    private final DateTimeFormatter dateTimeFormatter;

    @Autowired
    public ScreeningCommand(
        DateTimeFormatter dateTimeFormatter,
        ScreeningService screeningService,
        AuthService authService) {
        super(authService);
        this.dateTimeFormatter = dateTimeFormatter;
        this.screeningService = screeningService;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Creates a new screening", key = "create screening")
    public String createScreening(@ShellOption String movieName, @ShellOption String roomName,
                                  @ShellOption String screeningTime) throws IOException {
        try {
            screeningService.createScreening(movieName, roomName, screeningTime);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }

        return "Screening has been created";
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Deletes a screening", key = "delete screening")
    public String deleteScreening(@ShellOption String movieName,
                                  @ShellOption String roomName,
                                  @ShellOption String screeningTime) {
        try {
            screeningService.deleteScreening(movieName, roomName, screeningTime);

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
            .map(this::toStringScreeningData)
            .collect(Collectors.toList());
    }

    private String toStringScreeningData(Screening screening) {
        return String.format("%s, screened in room %s, at %s",
            screening.getMovie(),
            screening.getRoom().getName(),
            dateTimeFormatter.format(screening.getScreeningTime()));
    }
}
