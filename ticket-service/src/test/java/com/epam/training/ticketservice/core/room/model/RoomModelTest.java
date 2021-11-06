package com.epam.training.ticketservice.core.room.model;

import com.epam.training.ticketservice.core.pricing.model.PriceComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RoomModelTest {

    @Test
    public void testEqualsBasedOnRoomNameShouldReturnTrue() {
        // Given
        var room = new Room("Pedersoli", 10, 12);
        var otherRoom = new Room("Pedersoli", 10, 12);
        // When
        var result = room.equals(otherRoom);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsBasedOnRoomNameShouldReturnFalse() {
        // Given
        var room = new Room("Pedersoli", 10, 12);
        var otherRoom = new Room("Pedersoli 2", 10, 12);
        // When
        var result = room.equals(otherRoom);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentTypesShouldReturnFalse() {
        // Given
        var room = new Room("Pedersoli", 10, 12);
        var otherType = "a string";
        // When
        var result = room.equals(otherType);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testEqualsSameObjectShouldReturnTrue() {
        // Given
        var room = new Room("Pedersoli", 10, 12);
        var otherRoom = room;
        // When
        var result = room.equals(otherRoom);
        // Then
        Assertions.assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        // Given
        var room = new Room("Pedersoli", 10, 12);
        Room otherScreening = null;
        // When
        var result = room.equals(otherScreening);
        // Then
        Assertions.assertFalse(result);
    }

    @Test
    public void testHashCodeShouldReturnTheSameValueBasedOnRoomName() {
        // Given
        var room = new Room("Pedersoli", 10, 12);
        var otherRoom = new Room("Pedersoli", 10, 12);
        // When
        var hashCode = room.hashCode();
        var otherHashCode = otherRoom.hashCode();
        // Then
        Assertions.assertEquals(hashCode, otherHashCode);
    }


    @Test
    public void testSetNameShouldSetValue() {
        // Given
        var room = new Room("Pedersoli ", 10, 5);
        var newName = "Pedersoli updated";
        // When
        room.setName(newName);
        // Then
        Assertions.assertEquals(newName, room.getName());
    }

    @Test
    public void testSetRowsShouldSetValue() {
        // Given
        var room = new Room("Pedersoli ", 10, 5);
        var newRows = 15;
        // When
        room.setRows(newRows);
        // Then
        Assertions.assertEquals(newRows, room.getRows());
    }

    @Test
    public void testSetColumnsShouldSetValue() {
        // Given
        var room = new Room("Pedersoli ", 10, 5);
        var newColumns = 7;
        // When
        room.setColumns(newColumns);
        // Then
        Assertions.assertEquals(newColumns, room.getColumns());
    }

    @Test
    public void testSetPriceComponentShouldSetValue() {
        // Given
        var room = new Room("Pedersoli ", 10, 5);
        var priceComponent = new PriceComponent("Discount", 100L);
        // When
        room.setPriceComponent(priceComponent);
        // Then
        Assertions.assertEquals(priceComponent, room.getPriceComponent());
    }

    @Test
    public void testToStringShouldReturnProperString() {
        // Given
        var room = new Room("Pedersoli ", 10, 5);
        var expected = String.format("Room %s with %d seats, %d rows and %d columns",
            room.getName(),
            room.getRows() * room.getColumns(),
            room.getRows(),
            room.getColumns());
        // When
        var str = room.toString();
        // Then
        Assertions.assertEquals(expected, str);
    }
}
