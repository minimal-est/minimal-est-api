package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.application.file.FileServiceException;
import kr.minimalest.api.application.file.FileValidator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DefaultFileValidator implements FileValidator {

    private static final Set<String> PERMITTED_MIME_TYPES = Set.of(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "image/gif",
            "image/heic",
            "image/heif"
    );

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    @Override
    public void validate(String mimeType, long fileSize) {
        validateMimeType(mimeType);
        validateFileSize(fileSize);
    }

    private void validateMimeType(String mimeType) {
        if (!PERMITTED_MIME_TYPES.contains(mimeType)) {
            throw new FileServiceException("허용되지 않는 타입입니다. 허용되는 타입: " + PERMITTED_MIME_TYPES);
        }
    }

    private void validateFileSize(long fileSize) {
        if (fileSize > MAX_FILE_SIZE) {
            throw new FileServiceException("파일 크기가 너무 큽니다. 최대 크기: " + (MAX_FILE_SIZE / (1024 * 1024)) + "MB");
        }
    }
}
