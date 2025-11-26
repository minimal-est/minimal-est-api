package kr.minimalest.api.web.controller.dto.request;

public record PresignedUrlRequest(
        String fileName,
        String mimeType,
        long fileSize
) {
}
