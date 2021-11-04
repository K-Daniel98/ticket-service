package com.epam.training.ticketservice.core.movie.model;

import com.epam.training.ticketservice.core.pricing.model.PriceComponent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Movie {

    @NonNull
    @Id
    private String name;
    @NonNull
    private String type;
    @NonNull
    private Long length;

    @OneToOne(cascade = CascadeType.ALL)
    private PriceComponent priceComponent;

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
