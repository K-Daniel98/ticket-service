package com.epam.training.ticketservice.core.booking.model;

import com.epam.training.ticketservice.core.screening.model.Screening;
import com.epam.training.ticketservice.core.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    @ManyToOne
    private Screening screening;

    @NonNull
    @ManyToOne
    private User user;

    @NonNull
    private Long price;

    @NonNull
    private Integer rowNumber;

    @NonNull
    private Integer columnNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Booking booking = (Booking) o;

        return rowNumber.equals(booking.rowNumber)
            && columnNumber.equals(booking.columnNumber)
            && screening.equals(booking.screening);
    }

    @Override
    public int hashCode() {
        return Objects.hash(screening, rowNumber, columnNumber);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", rowNumber, columnNumber);
    }

}
