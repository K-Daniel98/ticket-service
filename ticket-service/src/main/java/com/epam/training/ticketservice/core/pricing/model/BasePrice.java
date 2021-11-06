package com.epam.training.ticketservice.core.pricing.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class BasePrice {
    private long amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BasePrice basePrice = (BasePrice) o;
        return amount == basePrice.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
