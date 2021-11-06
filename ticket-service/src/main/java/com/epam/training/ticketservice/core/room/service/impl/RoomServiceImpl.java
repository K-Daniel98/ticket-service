package com.epam.training.ticketservice.core.room.service.impl;

import com.epam.training.ticketservice.core.room.exception.RoomAlreadyExistException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.room.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void createRoom(String name, int rows, int columns) {
        if (roomRepository.existsById(name)) {
            throw new RoomAlreadyExistException(name);
        }
        roomRepository.save(new Room(name, rows, columns));
    }

    @Override
    public void updateRoom(String name, int rows, int columns) {
        if (!roomRepository.existsById(name)) {
            throw new RoomDoesNotExistException(name);
        }
        roomRepository.save(new Room(name, rows, columns));
    }

    @Override
    public void deleteRoom(String name) {
        if (!roomRepository.existsById(name)) {
            throw new RoomDoesNotExistException(name);
        }
        roomRepository.deleteById(name);
    }

    @Override
    public boolean exists(String name) {
        return roomRepository.existsById(name);
    }

    @Override
    public List<Room> listRooms() {
        return roomRepository.findAll();
    }
}
