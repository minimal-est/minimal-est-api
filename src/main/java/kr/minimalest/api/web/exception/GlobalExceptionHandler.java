package kr.minimalest.api.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.api.application.exception.AuthenticateUserException;
import kr.minimalest.api.web.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        log.error("예상치 못한 오류 {}: {}", request.getRequestURI(), e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(500),
                Title.of("서버 오류"),
                Detail.of("요청을 처리하는 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.")
        );
        return ResponseEntity.internalServerError().body(errorResponse);
    }

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

        return ResponseEntity
                .badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(AuthenticateUserException.class)
    public ResponseEntity<?> handleWebException(AuthenticateUserException e) {
        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(401),
                Title.of("사용자 인증 과정 실패"),
                Detail.of(e.getMessage())
        );
        return ResponseEntity
                .status(401)
                .body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleWebException(UnauthorizedException e) {
        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(401),
                Title.of("인증 실패"),
                Detail.of(e.getMessage())
        );
        return ResponseEntity
                .status(401)
                .body(errorResponse);
    }
}
