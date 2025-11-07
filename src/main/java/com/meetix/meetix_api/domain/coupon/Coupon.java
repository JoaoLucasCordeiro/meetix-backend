package com.meetix.meetix_api.domain.coupon;


import com.meetix.meetix_api.domain.event.Event;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Coupon")

public class Coupon {

    @Id
    @GeneratedValue()
    private UUID id;
    private String code;
    private Integer discount;
    private LocalDateTime valid;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

}
