package com.meetix.meetix_api.domain.coupon;

import com.meetix.meetix_api.domain.event.Event;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Table(name = "coupon")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class coupon {
    @Id
    @GeneratedValue
    private UUID id_coupon;

    private String code;
    private String discount;
    private Date valid;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
