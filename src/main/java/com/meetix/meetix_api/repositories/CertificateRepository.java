package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.domain.certificate.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {
    Optional<Certificate> findByValidationCode(UUID validationCode); // usca um certificado usando o código de validação único.

    Optional<Certificate> findByParticipantId(UUID participantId);
    //Busca um certificado pelo ID da participação
    //Usado para verificar se um certificado já foi emitido para um participante
}
