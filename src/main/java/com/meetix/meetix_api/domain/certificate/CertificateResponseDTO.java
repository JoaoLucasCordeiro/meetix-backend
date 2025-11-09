package com.meetix.meetix_api.domain.certificate;

import java.time.LocalDateTime;
import java.util.UUID;

public record CertificateResponseDTO(
        UUID validationCode,
        LocalDateTime issueDate,
        String participantName,
        String eventName,
        LocalDateTime eventStartDate,
        LocalDateTime eventEndDate
) {}
