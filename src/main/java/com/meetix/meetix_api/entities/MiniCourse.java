package com.meetix.meetix_api.entities;

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

public class MiniCourse extends Event {

    private String topic;

    private String instructor;

    private Integer workloadHours; // carga horária
    
    private boolean certificate;
}
