package kr.minimalest.api.application.file;

import java.time.LocalDateTime;

public record PresignedUrlResult(
        String presignedUrl,
        String objectKey,
        String filename,
        LocalDateTime expiresAt
) {
}
