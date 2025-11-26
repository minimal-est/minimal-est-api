package kr.minimalest.api.web.controller.dto.response;

import kr.minimalest.api.application.file.PresignedUrlResult;

import java.time.LocalDateTime;

public record PresignedUrlResponse (
    String presignedUrl,
    String objectKey,
    String fileName,
    LocalDateTime expiresAt
) {
    public static PresignedUrlResponse of(PresignedUrlResult result) {
        return new PresignedUrlResponse(
                result.presignedUrl(), result.objectKey(), result.filename(), result.expiresAt()
        );
    }
}
