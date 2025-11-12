package com.meetix.meetix_api.service;

import com.meetix.meetix_api.domain.certificate.Certificate;
import com.meetix.meetix_api.domain.certificate.CertificateResponseDTO;
import com.meetix.meetix_api.domain.event.Event;
import com.meetix.meetix_api.domain.event.EventParticipant;
import com.meetix.meetix_api.domain.user.User;
import com.meetix.meetix_api.exception.certificate.CertificateAlreadyExistsException;
import com.meetix.meetix_api.exception.certificate.CertificateGenerationNotAllowedException;
import com.meetix.meetix_api.exception.certificate.CertificateNotFoundException;
import com.meetix.meetix_api.repositories.CertificateRepository;
import com.meetix.meetix_api.repositories.EventParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final PDFGeneratorService pdfGeneratorService;

    @Transactional
    public CertificateResponseDTO generateCertificate(UUID participantId) {
        EventParticipant participant = eventParticipantRepository.findById(participantId)
                .orElseThrow(() -> new CertificateNotFoundException("Participação não encontrada com ID: " + participantId));

        if (certificateRepository.existsByParticipantId(participantId)) {
            throw new CertificateAlreadyExistsException("Certificado já foi gerado para este participante");
        }

        validateCertificateGeneration(participant);

        UUID validationCode = UUID.randomUUID();
        Certificate certificate = new Certificate(participant, validationCode);

        Certificate savedCertificate = certificateRepository.save(certificate);

        return mapToResponseDTO(savedCertificate);
    }

    @Transactional(readOnly = true)
    public CertificateResponseDTO getCertificateByValidationCode(UUID validationCode) {
        Certificate certificate = certificateRepository.findByValidationCode(validationCode)
                .orElseThrow(() -> new CertificateNotFoundException("Certificado não encontrado com o código de validação: " + validationCode));

        return mapToResponseDTO(certificate);
    }

    @Transactional(readOnly = true)
    public CertificateResponseDTO getCertificateByParticipantId(UUID participantId) {
        Certificate certificate = certificateRepository.findByParticipantId(participantId)
                .orElseThrow(() -> new CertificateNotFoundException("Certificado não encontrado para o participante com ID: " + participantId));

        return mapToResponseDTO(certificate);
    }

    @Transactional(readOnly = true)
    public byte[] downloadCertificatePDF(UUID validationCode) {
        Certificate certificate = certificateRepository.findByValidationCode(validationCode)
                .orElseThrow(() -> new CertificateNotFoundException("Certificado não encontrado com o código de validação: " + validationCode));

        EventParticipant participant = certificate.getParticipant();
        Event event = participant.getEvent();
        User user = participant.getUser();
        User organizer = event.getOrganizer();

        String participantName = user.getFirstName() + " " + user.getLastName();
        String organizerName = organizer.getFirstName() + " " + organizer.getLastName();

        return pdfGeneratorService.generateCertificatePDF(
                participantName,
                event.getTitle(),
                event.getStartDateTime(),
                event.getEndDateTime(),
                organizerName,
                certificate.getValidationCode(),
                certificate.getIssueDate()
        );
    }

    private void validateCertificateGeneration(EventParticipant participant) {
        Event event = participant.getEvent();

        if (!event.getGenerateCertificate()) {
            throw new CertificateGenerationNotAllowedException("Este evento não está configurado para gerar certificados");
        }

        if (!participant.getAttended()) {
            throw new CertificateGenerationNotAllowedException("O participante não marcou presença no evento");
        }

        LocalDateTime now = LocalDateTime.now();
        if (event.getEndDateTime().isAfter(now)) {
            throw new CertificateGenerationNotAllowedException("O evento ainda não foi finalizado");
        }
    }

    private CertificateResponseDTO mapToResponseDTO(Certificate certificate) {
        EventParticipant participant = certificate.getParticipant();
        Event event = participant.getEvent();
        User user = participant.getUser();

        String participantName = user.getFirstName() + " " + user.getLastName();

        return new CertificateResponseDTO(
                certificate.getValidationCode(),
                certificate.getIssueDate(),
                participantName,
                event.getTitle(),
                event.getStartDateTime(),
                event.getEndDateTime()
        );
    }
}
