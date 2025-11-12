package com.meetix.meetix_api.domain.certificate;

import com.meetix.meetix_api.domain.event.EventParticipant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "certificates")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue
    @Column(name = "certificate_id")
    private UUID certificateId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", nullable = false, unique = true)
    private EventParticipant participant;

    @Column(name = "validation_code", nullable = false, unique = true)
    private UUID validationCode;

    @CreationTimestamp
    @Column(name = "issue_date", nullable = false, updatable = false)
    private LocalDateTime issueDate;

    public Certificate(EventParticipant participant, UUID validationCode) {
        this.participant = participant;
        this.validationCode = validationCode;
    }

}
