package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.domain.certificate.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, UUID> {

    Optional<Certificate> findByValidationCode(UUID validationCode);

    Optional<Certificate> findByParticipantId(UUID participantId);

    boolean existsByParticipantId(UUID participantId);
}
