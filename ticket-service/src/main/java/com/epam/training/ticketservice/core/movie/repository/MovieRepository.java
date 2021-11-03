package com.epam.training.ticketservice.core.movie.repository;

import com.epam.training.ticketservice.core.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, String> {

    Optional<Movie> findByName(String name);

    @Override
    List<Movie> findAll();
}

