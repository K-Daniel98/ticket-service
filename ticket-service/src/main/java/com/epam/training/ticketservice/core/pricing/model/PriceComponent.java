package com.epam.training.ticketservice.core.pricing.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class PriceComponent {

    @NonNull
    @Id
    private String name;

    @NonNull
    private Long amount;

}
