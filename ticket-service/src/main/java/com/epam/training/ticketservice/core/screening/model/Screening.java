package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.configuration.ApplicationConfiguration;
import com.epam.training.ticketservice.core.movie.model.Movie;
import com.epam.training.ticketservice.core.room.model.Room;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Movie movie;

    @ManyToOne
    private Room room;

    private LocalDateTime screeningTime;

    public Screening(Movie movie, Room room, LocalDateTime screeningTime) {
        this.movie = movie;
        this.room = room;
        this.screeningTime = screeningTime;
    }

    protected Screening() {
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDateTime getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(LocalDateTime screeningTime) {
        this.screeningTime = screeningTime;
    }

    @Override
    public String toString() {
        return String.format("%s, screened in room %s, at %s", movie.toString(), room.getName(),
            ApplicationConfiguration.formatter.format(screeningTime));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Screening screening = (Screening) o;
        return movie.equals(screening.movie) && room.equals(screening.room) &&
            screeningTime.equals(screening.screeningTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, room, screeningTime);
    }
}
