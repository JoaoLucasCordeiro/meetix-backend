package com.meetix.meetix_api.service;

import com.lowagie.text.DocumentException;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PDFGeneratorService {

    private final TemplateEngine templateEngine;

    public PDFGeneratorService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generateCertificatePDF(
            String participantName,
            String eventName,
            LocalDateTime eventStartDate,
            LocalDateTime eventEndDate,
            String organizerName,
            UUID validationCode,
            LocalDateTime issueDate
    ) {
        try {
            Context context = new Context();
            context.setVariable("participantName", participantName);
            context.setVariable("eventName", eventName);
            context.setVariable("eventStartDate", eventStartDate);
            context.setVariable("eventEndDate", eventEndDate);
            context.setVariable("organizerName", organizerName);
            context.setVariable("validationCode", validationCode.toString());
            context.setVariable("issueDate", issueDate);

            String htmlContent = templateEngine.process("certificate-template", context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            renderer.finishPDF();

            return outputStream.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage(), e);
        }
    }
}
