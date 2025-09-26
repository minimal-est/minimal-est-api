package kr.minimalest.api.application.file;

public interface FileValidator {

    void validate(String mimeType, long fileSize);
}
