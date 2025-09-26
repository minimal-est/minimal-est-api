package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.application.file.PresignedUrlGenerator;
import kr.minimalest.api.application.file.PresignedUrlResult;
import kr.minimalest.api.infrastructure.aws.s3.S3PropertiesProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class S3PresginedUrlGenerator implements PresignedUrlGenerator {

    private final S3Client s3Client;
    private final S3PropertiesProvider propertiesProvider;

    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofMinutes(15);

    @Override
    public PresignedUrlResult generatePresignedUrl(String objectKey, String mimeType, long fileSize, String fileName) {
        try(S3Presigner presigner = createS3Presigner()) {

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(propertiesProvider.bucket())
                    .key(objectKey)
                    .contentType(mimeType)
                    .contentLength(fileSize)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(PRESIGNED_URL_EXPIRATION)
                    .putObjectRequest(putObjectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);

            return new PresignedUrlResult(
                    presignedRequest.url().toString(),
                    objectKey,
                    fileName,
                    LocalDateTime.now().plus(PRESIGNED_URL_EXPIRATION)
            );
        }
    }

    private S3Presigner createS3Presigner() {
        return S3Presigner.builder()
                .region(s3Client.serviceClientConfiguration().region())
                .credentialsProvider(s3Client.serviceClientConfiguration().credentialsProvider())
                .build();
    }
}
