package com.epam.training.ticketservice.core.room.repository;

import com.epam.training.ticketservice.core.room.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, String> {

    Optional<Room> findByName(String name);

    @Override
    List<Room> findAll();
}
