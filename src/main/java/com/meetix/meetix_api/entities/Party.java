package com.meetix.meetix_api.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor
@DiscriminatorValue("PARTY")

public class Party extends Event {

    private String organizer;

    private boolean openBar;
}
