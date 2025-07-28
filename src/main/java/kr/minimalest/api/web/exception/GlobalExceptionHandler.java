package kr.minimalest.api.web.exception;

import kr.minimalest.api.web.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(400),
                Title.of("HTTP 메시지 읽기 실패"),
                Detail.of("올바른 JSON 형식이 아니거나, 요청에 필요한 데이터가 비어있습니다.")
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }));

        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(400),
                Title.of("입력값 검증 실패"),
                Detail.of("하나 이상의 필드가 유효하지 않습니다."),
                Properties.of("errors", errors)
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }
}
