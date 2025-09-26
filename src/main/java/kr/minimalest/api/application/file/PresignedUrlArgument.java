package kr.minimalest.api.application.file;

import org.springframework.util.Assert;

public record PresignedUrlArgument(
        String originalFileName,
        String mimeType,
        long fileSize
) {
    public PresignedUrlArgument {
        Assert.hasText(originalFileName, "파일 이름이 비어있습니다.");
        Assert.hasText(mimeType, "타입이 비어있습니다.");
        Assert.state(fileSize > 0, "파일 크기는 0보다 커야합니다.");
    }

    public static PresignedUrlArgument of(String originalFileName, String mimeType, long fileSize) {
        return new PresignedUrlArgument(originalFileName, mimeType, fileSize);
    }
}
