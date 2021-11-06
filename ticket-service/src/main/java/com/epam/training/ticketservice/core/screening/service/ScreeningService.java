package com.epam.training.ticketservice.core.screening.service;

import com.epam.training.ticketservice.core.screening.model.Screening;

import java.util.List;

public interface ScreeningService {

    void createScreening(String movieName,
                         String roomName,
                         String screeningTime);

    void updateScreening(Screening screening);

    void deleteScreening(String movieName,
                         String roomName,
                         String screeningTime);

    void deleteScreening(Screening screening);

    List<Screening> listScreenings();

}
