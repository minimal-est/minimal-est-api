package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.application.file.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3FileService implements FileService {

    private final FileValidator fileValidator;
    private final FilePathGenerator pathGenerator;
    private final PresignedUrlGenerator urlGenerator;

    @Override
    public PresignedUrlResult generatePresignedUrl(PresignedUrlArgument argument) {
        fileValidator.validate(argument.mimeType(), argument.fileSize());

        String fileName = pathGenerator.generateUniqueFileName(argument.originalFileName());
        String objectKey = pathGenerator.generateObjectKey(fileName);

        return urlGenerator.generatePresignedUrl(
                objectKey,
                argument.mimeType(),
                argument.fileSize(),
                fileName
        );
    }
}
