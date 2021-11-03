package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.room.model.Room;

import java.util.List;
import java.util.Optional;

public interface RoomService {
    void createRoom(Room room);

    void updateRoom(Room room);

    void deleteRoom(String name);

    Optional<Room> getRoomByName(String name);

    boolean exists(String name);

    List<Room> listRooms();
}
