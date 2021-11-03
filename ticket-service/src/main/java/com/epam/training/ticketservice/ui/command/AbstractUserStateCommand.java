package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.user.model.User;
import org.springframework.shell.Availability;

public abstract class AbstractUserStateCommand {

    protected final AuthService authService;

    public AbstractUserStateCommand(AuthService authService) {
        this.authService = authService;
    }

    protected Availability loggedIn() {
        return isLoggedIn()
            ? Availability.available()
            : Availability.unavailable("You are not signed in"); // You need to login first
    }

    protected Availability admin() {
        return isLoggedIn() && isAdmin()
            ? Availability.available()
            : Availability.unavailable("You are not an admin");
    }

    protected Availability notAdmin() {
        return isLoggedIn() && !isAdmin()
            ? Availability.available()
            : Availability.unavailable(""); // Only regular user accounts can use this command
    }

    private boolean isLoggedIn() {
        return authService.getLoggedInUser().isPresent();
    }

    private boolean isAdmin() {
        return authService.getLoggedInUser().get().getRole() == User.Role.ADMIN;
    }

}
