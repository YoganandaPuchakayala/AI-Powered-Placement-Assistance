package com.aipoweredplacementassistant.service;

import java.io.InputStream;
import java.net.URL;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;


@Service
public class PdfParserService {

    public String extractText(String fileUrl) {

        try (InputStream input = new URL(fileUrl).openStream()) {

            byte[] pdfBytes = input.readAllBytes();

            try (PDDocument document = Loader.loadPDF(pdfBytes)) {

                PDFTextStripper stripper = new PDFTextStripper();

                return stripper.getText(document);
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to parse PDF", e);
        }
    }
}
