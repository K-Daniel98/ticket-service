package com.epam.training.ticketservice.core.movie.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Movie {

    @Id
    private String name;
    private String type;
    private long length;

    public Movie(String name, String type, long length) {
        this.name = name;
        this.type = type;
        this.length = length;
    }

    protected Movie() {
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public long getLength() {
        return length;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLength(long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return String.format("%s (%s, %d minutes)", name, type, length);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Movie movie = (Movie) o;
        return name.equals(movie.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
