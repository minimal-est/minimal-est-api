package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.minimalest.api.application.file.FileService;
import kr.minimalest.api.application.file.PresignedUrlArgument;
import kr.minimalest.api.application.file.PresignedUrlResult;
import kr.minimalest.api.web.controller.dto.request.PresignedUrlRequest;
import kr.minimalest.api.web.controller.dto.response.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@Tag(name = "File", description = "파일 업로드 관련 API")
public class FileController {

    private final FileService fileService;

    @PostMapping("/presigned")
    @Operation(summary = "S3 프리사인드 URL 생성", description = "AWS S3에 파일을 업로드하기 위한 프리사인드 URL을 생성합니다.")
    public ResponseEntity<PresignedUrlResponse> generatePresignedUrl(
            @RequestBody PresignedUrlRequest request
    ) {
        PresignedUrlArgument argument = PresignedUrlArgument.of(
                request.fileName(),
                request.mimeType(),
                request.fileSize()
        );
        PresignedUrlResult result = fileService.generatePresignedUrl(argument);

        return ResponseEntity.ok(PresignedUrlResponse.of(result));
    }
}
