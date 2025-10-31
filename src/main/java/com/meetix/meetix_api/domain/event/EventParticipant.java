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
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Column(nullable = false)
    private Boolean attended = false;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    @PrePersist
    protected void onCreate() {
        registrationDate = LocalDateTime.now();
        if (attended == null) attended = false;
    }
}
