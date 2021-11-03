package com.epam.training.ticketservice.core.room.model;

import com.epam.training.ticketservice.core.screening.model.Screening;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Room {

    @Id
    private String name;
    private int rows;

    private int columns;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Screening> screenings;

    public Room(String name, int rows, int columns) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
    }

    protected Room() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<Screening> getScreenings() {
        return screenings;
    }

    public void setScreening(List<Screening> screenings) {
        this.screenings = screenings;
    }

    @Override
    public String toString() {
        return String.format("Room %s with %d seats, %d rows and %d columns", name, rows * columns, rows, columns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return name.equals(room.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
