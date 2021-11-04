package com.epam.training.ticketservice.core.pricing.repository;

import com.epam.training.ticketservice.core.pricing.model.PriceComponent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriceComponentRepository extends JpaRepository<PriceComponent, String> {
    Optional<PriceComponent> getPriceComponentByName(String name);
}
