package com.meetix.meetix_api.exception.certificate;

public class CertificateGenerationNotAllowedException extends RuntimeException {
    public CertificateGenerationNotAllowedException(String message) {
        super(message);
    }
}
