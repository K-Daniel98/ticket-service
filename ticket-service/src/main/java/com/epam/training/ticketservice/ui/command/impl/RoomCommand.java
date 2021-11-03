package com.epam.training.ticketservice.ui.command.impl;

import com.epam.training.ticketservice.core.user.service.AuthService;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.ui.command.AbstractUserStateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.List;
import java.util.stream.Collectors;

@ShellComponent
public class RoomCommand extends AbstractUserStateCommand {

    private final RoomService roomService;

    @Autowired
    public RoomCommand(RoomService roomService, AuthService authService) {
        super(authService);
        this.roomService = roomService;
    }

    @ShellMethodAvailability("loggedIn")
    @ShellMethod(value = "Creates a new room", key = "create room")
    public String createRoom(@ShellOption String roomName, @ShellOption int rows, @ShellOption int columns) {
        try {
            roomService.createRoom(new Room(roomName, rows, columns));
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return String.format("Room '%s' has been created.", roomName);
    }

    @ShellMethodAvailability("loggedIn")
    @ShellMethod(value = "Deletes a room", key = "delete room")
    public String deleteRoom(@ShellOption String name) {
        try {
            roomService.deleteRoom(name);
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return String.format("Room '%s' has been deleted.", name);
    }

    @ShellMethod(value = "Lists all rooms", key = "list rooms")
    public List<String> listMovies() {
        var rooms = roomService.listRooms();

        if (rooms.size() == 0) {
            return List.of("There are no movies at the moment");
        }

        return rooms
            .stream()
            .map(Room::toString)
            .collect(Collectors.toList());
    }

    @ShellMethodAvailability("loggedIn")
    @ShellMethod(value = "Updates a room", key = "update room")
    public String updateMovie(@ShellOption String roomName, @ShellOption int rows, @ShellOption int columns) {
        try {
            roomService.updateRoom(new Room(roomName, rows, columns));
        } catch (RuntimeException exception) {
            return exception.getMessage();
        }
        return String.format("Room '%s' has been updated.", roomName);
    }

}
