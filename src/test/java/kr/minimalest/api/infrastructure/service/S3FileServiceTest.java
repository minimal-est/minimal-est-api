package kr.minimalest.api.infrastructure.service;

import kr.minimalest.api.application.file.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class S3FileServiceTest {

    @Mock
    FileValidator fileValidator;

    @Mock
    FilePathGenerator filePathGenerator;

    @Mock
    PresignedUrlGenerator urlGenerator;

    @InjectMocks
    S3FileService fileService;

    @Test
    @DisplayName("성공적인 presignedUrl 발급")
    void shouldReturnSuccessPresignedUrl() {
        // given
        PresignedUrlArgument presignedUrlArgument = PresignedUrlArgument.of(
                "test.png",
                "image/png",
                1024 * 1024L
        );

        String uniqueFileName = UUID.randomUUID() + ".png";
        String objectKey = "uploads/" + uniqueFileName;

        PresignedUrlResult expectedResult = new PresignedUrlResult(
                "https://s3.aws.com/bucket/" + objectKey,
                objectKey,
                uniqueFileName,
                LocalDateTime.now().plus(Duration.ofMinutes(15))
        );

        doNothing().when(fileValidator).validate("image/png", 1024 * 1024L);

        given(filePathGenerator.generateUniqueFileName("test.png"))
                .willReturn(uniqueFileName);
        given(filePathGenerator.generateObjectKey(uniqueFileName))
                .willReturn(objectKey);
        given(urlGenerator.generatePresignedUrl(objectKey, "image/png", 1024 * 1024L, uniqueFileName))
                .willReturn(expectedResult);

        // when
        PresignedUrlResult result = fileService.generatePresignedUrl(presignedUrlArgument);

        // then
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }
}
