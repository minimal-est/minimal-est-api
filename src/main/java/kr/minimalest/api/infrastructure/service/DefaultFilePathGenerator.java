package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.application.file.FilePathGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Component
public class DefaultFilePathGenerator implements FilePathGenerator {

    @Override
    public String generateUniqueFileName(String originalFileName) {
        String extension = extractFileExtension(originalFileName);
        return UUID.randomUUID() + (extension.isEmpty() ? "" : "." + extension);
    }

    private String extractFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1).toLowerCase();
        }
        return "";
    }

    @Override
    public String generateObjectKey(String fileName) {
        String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        return String.format("uploads/%s/%s", datePath, fileName);
    }
}
