package com.meetix.meetix_api.domain.coupon;

import com.meetix.meetix_api.domain.event.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank
    private String code;

    @Column(nullable = false)
    @NotNull
    @Min(1)
    @Max(100)
    private Integer discount;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime valid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @NotNull
    private Event event;
}
