package com.meetix.meetix_api.domain.event;

import com.meetix.meetix_api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "event_participant")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventParticipant {
    @Id
    @GeneratedValue
    @Column(name = "id_event_participant")
    private UUID id_event_participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id_event")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id_user")
    private User user;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "attended")
    private Boolean attended;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;
}
