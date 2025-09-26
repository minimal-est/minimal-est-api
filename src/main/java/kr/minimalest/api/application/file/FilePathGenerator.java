package kr.minimalest.api.application.file;

public interface FilePathGenerator {
    String generateUniqueFileName(String originalFileName);

    String generateObjectKey(String fileName);
}
