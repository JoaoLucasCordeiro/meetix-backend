package com.meetix.meetix_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Table(name = "event")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue
    private UUID id_event;
    private String title;
    private String description;
    private BigDecimal price;
    private String imgUrl;
    private Integer maxAttendees;
    private String eventUrl;
    private Boolean remote;
    private Date date;
    private String address;

}
