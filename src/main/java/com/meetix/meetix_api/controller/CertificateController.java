package com.meetix.meetix_api.controller;

import com.meetix.meetix_api.domain.certificate.CertificateResponseDTO;
import com.meetix.meetix_api.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
@Tag(name = "Certificados", description = "Gerenciamento de certificados de eventos")
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/generate/{participantId}")
    @Operation(summary = "Gerar certificado", description = "Gera um certificado para um participante de evento")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Certificado gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Participação não encontrada"),
            @ApiResponse(responseCode = "409", description = "Certificado já existe para este participante")
    })
    public ResponseEntity<CertificateResponseDTO> generateCertificate(@PathVariable UUID participantId) {
        CertificateResponseDTO certificate = certificateService.generateCertificate(participantId);
        return ResponseEntity.status(HttpStatus.CREATED).body(certificate);
    }

    @GetMapping("/validation/{validationCode}")
    @Operation(summary = "Buscar certificado por código de validação", description = "Retorna os dados de um certificado através do código de validação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certificado encontrado"),
            @ApiResponse(responseCode = "404", description = "Certificado não encontrado")
    })
    public ResponseEntity<CertificateResponseDTO> getCertificateByValidationCode(@PathVariable UUID validationCode) {
        CertificateResponseDTO certificate = certificateService.getCertificateByValidationCode(validationCode);
        return ResponseEntity.ok(certificate);
    }

    @GetMapping("/participant/{participantId}")
    @Operation(summary = "Buscar certificado por participante", description = "Retorna o certificado de um participante")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certificado encontrado"),
            @ApiResponse(responseCode = "404", description = "Certificado não encontrado")
    })
    public ResponseEntity<CertificateResponseDTO> getCertificateByParticipantId(@PathVariable UUID participantId) {
        CertificateResponseDTO certificate = certificateService.getCertificateByParticipantId(participantId);
        return ResponseEntity.ok(certificate);
    }

    @GetMapping("/download/{validationCode}")
    @Operation(summary = "Download do certificado em PDF", description = "Faz o download do certificado em formato PDF usando o código de validação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "PDF gerado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Certificado não encontrado")
    })
    public ResponseEntity<byte[]> downloadCertificatePDF(@PathVariable UUID validationCode) {
        byte[] pdfBytes = certificateService.downloadCertificatePDF(validationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificate-" + validationCode + ".pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
