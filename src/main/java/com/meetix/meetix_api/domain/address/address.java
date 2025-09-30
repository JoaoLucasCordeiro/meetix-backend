package com.meetix.meetix_api.domain.address;

import com.meetix.meetix_api.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Table(name = "address")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class address {
    @Id
    @GeneratedValue
    private UUID id_address;

    private String city;
    private String uf;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
