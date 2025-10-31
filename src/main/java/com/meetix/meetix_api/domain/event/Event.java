package com.meetix.meetix_api.domain.event;

import com.meetix.meetix_api.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    private String location;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "event_url")
    private String eventUrl;

    @Column(nullable = false)
    private Boolean remote = false;

    @Column(name = "max_attendees")
    private Integer maxAttendees;

    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = false;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @Column(name = "generate_certificate", nullable = false)
    private Boolean generateCertificate = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (remote == null) remote = false;
        if (isPaid == null) isPaid = false;
        if (generateCertificate == null) generateCertificate = false;
        if (imgUrl == null || imgUrl.isBlank()) {
            imgUrl = "https://placeholder.com/default-image.png";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
