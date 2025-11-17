package kr.minimalest.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.minimalest.api.application.file.FileService;
import kr.minimalest.api.application.file.PresignedUrlArgument;
import kr.minimalest.api.application.file.PresignedUrlResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
@Tag(name = "File", description = "파일 업로드 관련 API")
public class FileController {

    private final FileService fileService;

    @PostMapping("/presigned")
    @Operation(summary = "S3 프리사인드 URL 생성", description = "AWS S3에 파일을 업로드하기 위한 프리사인드 URL을 생성합니다.")
    public ResponseEntity<?> generatePresignedUrl(
            @RequestParam("fileName") String fileName,
            @RequestParam("mimeType") String mimeType,
            @RequestParam("fileSize") long fileSize
    ) {
        PresignedUrlArgument argument = PresignedUrlArgument.of(fileName, mimeType, fileSize);
        PresignedUrlResult result = fileService.generatePresignedUrl(argument);

        return ResponseEntity.ok(result);
    }
}
