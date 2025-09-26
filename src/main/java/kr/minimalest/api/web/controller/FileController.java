package kr.minimalest.api.web.controller;

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
public class FileController {

    private final FileService fileService;

    @PostMapping("/presigned")
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
