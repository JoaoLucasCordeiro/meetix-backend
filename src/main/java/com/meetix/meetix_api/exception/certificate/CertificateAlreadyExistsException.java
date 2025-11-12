package com.meetix.meetix_api.exception.certificate;

public class CertificateAlreadyExistsException extends RuntimeException {
    public CertificateAlreadyExistsException(String message) {
        super(message);
    }
}
