package com.epam.training.ticketservice.ui.command;

import com.epam.training.ticketservice.core.room.exception.RoomAlreadyExistException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.service.RoomService;
import com.epam.training.ticketservice.ui.command.impl.RoomCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RoomCommandTest {

    @Mock
    private RoomService roomService;

    @InjectMocks
    private RoomCommand roomCommand;

    @Test
    public void testCreateRoomShouldThrowRoomAlreadyExistException() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;

        var exception = new RoomAlreadyExistException(roomName);
        // When
        Mockito.doThrow(exception).when(roomService).createRoom(roomName, rows, cols);
        // Then
        Assertions.assertEquals(exception.getMessage(), roomCommand.createRoom(roomName, rows, cols));
    }

    @Test
    public void testCreateRoomShouldSucceed() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;
        // When
        Mockito.doNothing().when(roomService).createRoom(roomName, rows, cols);
        // Then
        Assertions.assertNull(roomCommand.createRoom(roomName, rows, cols));
    }

    @Test
    public void testUpdateRoomShouldThrowRoomDoesNotExistException() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;

        var exception = new RoomAlreadyExistException(roomName);
        // When
        Mockito.doThrow(exception).when(roomService).updateRoom(roomName, rows, cols);
        // Then
        Assertions.assertEquals(exception.getMessage(), roomCommand.updateMovie(roomName, rows, cols));
    }

    @Test
    public void testUpdateRoomShouldSucceed() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;
        // When
        Mockito.doNothing().when(roomService).updateRoom(roomName, rows, cols);
        // Then
        Assertions.assertNull(roomCommand.updateMovie(roomName, rows, cols));
    }

    @Test
    public void testDeleteRoomShouldThrowRoomDoesNotExistException() {
        // Given
        var roomName = "Pedersoli";
        var exception = new RoomDoesNotExistException(roomName);
        // When
        Mockito.doThrow(exception).when(roomService).deleteRoom(roomName);
        // Then
        Assertions.assertEquals(exception.getMessage(), roomCommand.deleteRoom(roomName));
    }

    @Test
    public void testDeleteRoomShouldSucceed() {
        // Given
        var roomName = "Pedersoli";
        // When
        Mockito.doNothing().when(roomService).deleteRoom(roomName);
        // Then
        Assertions.assertNull(roomCommand.deleteRoom(roomName));
    }

    @Test
    public void testListRoomsShouldReturnNonEmptyList() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var cols = 10;

        var room = new Room(roomName, rows, cols);
        var anotherRoom = new Room(roomName, rows, cols);
        // When
        Mockito.when(roomService.listRooms()).thenReturn(List.of(room, anotherRoom));
        // Then
        Assertions.assertEquals(2, roomCommand.listRooms().size());
    }

    @Test
    public void testListRoomsShouldReturnEmptyList() {
        // Given
        var expected = List.of("There are no rooms at the moment");
        // When
        Mockito.when(roomService.listRooms()).thenReturn(List.of());
        // Then
        Assertions.assertEquals(expected, roomCommand.listRooms());
    }

}
