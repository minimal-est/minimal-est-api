package kr.minimalest.api.application.file;

public interface FileService {

    PresignedUrlResult generatePresignedUrl(PresignedUrlArgument argument);
}
