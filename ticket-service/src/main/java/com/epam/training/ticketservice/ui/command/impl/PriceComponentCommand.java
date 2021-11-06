package com.epam.training.ticketservice.ui.command.impl;

import com.epam.training.ticketservice.core.pricing.model.BasePrice;
import com.epam.training.ticketservice.core.pricing.service.PriceComponentService;
import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.utils.formatter.DateTimeFormatterUtil;
import com.epam.training.ticketservice.ui.command.AbstractUserStateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class PriceComponentCommand extends AbstractUserStateCommand {

    private final PriceComponentService priceComponentService;
    private final BasePrice basePrice;
    private final DateTimeFormatterUtil dateTimeFormatterUtil;

    @Autowired
    public PriceComponentCommand(AuthService authService, PriceComponentService priceComponentService,
                                 BasePrice basePrice, DateTimeFormatterUtil dateTimeFormatterUtil) {
        super(authService);
        this.priceComponentService = priceComponentService;
        this.basePrice = basePrice;
        this.dateTimeFormatterUtil = dateTimeFormatterUtil;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Updates the base price", key = "update base price")
    public String updateBasePrice(@ShellOption long amount) {
        basePrice.setAmount(amount);
        return null;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Creates a new price component", key = "create price component")
    public String createPriceComponent(@ShellOption String priceComponentName, @ShellOption long amount) {
        try {
            priceComponentService.createPriceComponent(priceComponentName, amount);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return null;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Attaches a price component to a movie", key = "attach price component to movie")
    public String attachPriceComponentToMovie(@ShellOption String priceComponentName, @ShellOption String movieName) {
        try {
            priceComponentService.attachPriceComponentToMovie(priceComponentName, movieName);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return null;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Attaches a price component to a room", key = "attach price component to room")
    public String attachPriceComponentToRoom(@ShellOption String priceComponentName, @ShellOption String roomName) {
        try {
            priceComponentService.attachPriceComponentToRoom(priceComponentName, roomName);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return null;
    }

    @ShellMethodAvailability("admin")
    @ShellMethod(value = "Attaches a price component to a screening", key = "attach price component to screening")
    public String attachPriceComponentToScreening(@ShellOption String priceComponentName,
                                                  @ShellOption String movieName,
                                                  @ShellOption String roomName,
                                                  @ShellOption String screeningTime) {
        try {
            var screening =
                priceComponentService.attachPriceComponentToScreening(priceComponentName, movieName, roomName,
                    screeningTime);
            return String.format("Attached price component '%s' to screening '%s, screened in room %s, at %s'",
                priceComponentName,
                screening.getMovie(),
                screening.getRoom().getName(),
                dateTimeFormatterUtil.fromLocalDateTime(screening.getScreeningTime()));
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
    }

}
