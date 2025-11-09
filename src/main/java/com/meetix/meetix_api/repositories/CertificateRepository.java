package com.meetix.meetix_api.repositories;

import com.meetix.meetix_api.domain.certificate.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {

}
