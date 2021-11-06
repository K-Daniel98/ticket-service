package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.room.model.Room;

import java.util.List;

public interface RoomService {
    void createRoom(String name, int rows, int columns);

    void updateRoom(String name, int rows, int columns);

    void deleteRoom(String name);

    boolean exists(String name);

    List<Room> listRooms();
}
