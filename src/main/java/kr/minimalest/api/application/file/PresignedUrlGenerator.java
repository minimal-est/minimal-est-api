package kr.minimalest.api.application.file;

public interface PresignedUrlGenerator {

    PresignedUrlResult generatePresignedUrl(String objectKey, String mimeType, long fileSize, String fileName);
}
