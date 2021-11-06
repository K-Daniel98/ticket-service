package com.epam.training.ticketservice.core.room.service;

import com.epam.training.ticketservice.core.booking.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.exception.RoomAlreadyExistException;
import com.epam.training.ticketservice.core.room.exception.RoomDoesNotExistException;
import com.epam.training.ticketservice.core.room.model.Room;
import com.epam.training.ticketservice.core.room.repository.RoomRepository;
import com.epam.training.ticketservice.core.room.service.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.screening.repository.ScreeningRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RoomServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    @Test
    public void testCreateRoomShouldThrowRoomAlreadyExistException() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var columns = 5;
        // When
        Mockito.when(roomRepository.existsById(roomName)).thenReturn(true);
        // Then
        Assertions.assertThrows(RoomAlreadyExistException.class,
            () -> roomService.createRoom(roomName, rows, columns));
    }

    @Test
    public void testCreateRoomShouldSucceed() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var columns = 5;
        // When
        Mockito.when(roomRepository.existsById(roomName)).thenReturn(false);
        // Then
        Assertions.assertDoesNotThrow(() -> roomService.createRoom(roomName, rows, columns));
    }

    @Test
    public void testUpdateRoomShouldThrowRoomDoesNotExistException() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var columns = 5;

        var room = new Room(roomName, rows, columns);
        // When
        Mockito.when(roomRepository.existsById(roomName)).thenReturn(false);
        // Then
        Assertions.assertThrows(RoomDoesNotExistException.class, () -> roomService.updateRoom(roomName, rows, columns));
    }

    @Test
    public void testUpdateRoomShouldSucceed() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var columns = 5;
        // When
        Mockito.when(roomRepository.existsById(roomName)).thenReturn(true);
        // Then
        Assertions.assertDoesNotThrow(() -> roomService.updateRoom(roomName, rows, columns));
    }

    @Test
    public void testExistsRoomByName() {
        // Given
        var roomName = "Pedersoli";
        // When
        Mockito.when(roomRepository.existsById(roomName)).thenReturn(true);
        // Then
        Assertions.assertTrue(roomService.exists(roomName));
    }

    @Test
    public void testListRoomsShouldReturnNonEmptyList() {
        // Given
        var roomName = "Pedersoli";
        var rows = 10;
        var columns = 5;

        var room = new Room(roomName, rows, columns);
        // When
        Mockito.when(roomRepository.findAll()).thenReturn(List.of(room));
        // Then
        Assertions.assertFalse(roomService.listRooms().isEmpty());
    }
}
