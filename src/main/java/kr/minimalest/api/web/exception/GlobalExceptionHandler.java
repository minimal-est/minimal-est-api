package kr.minimalest.api.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import kr.minimalest.api.application.file.FileServiceException;
import kr.minimalest.api.domain.publishing.exception.AuthorNotFoundException;
import kr.minimalest.api.domain.publishing.exception.BlogNotFoundException;
import kr.minimalest.api.domain.writing.exception.ArticleAccessDeniedException;
import kr.minimalest.api.domain.writing.exception.ArticleCompleteFailException;
import kr.minimalest.api.domain.writing.exception.ArticleNotFoundException;
import kr.minimalest.api.domain.writing.exception.ArticleStateException;
import kr.minimalest.api.domain.publishing.exception.PenNameAlreadyExists;
import kr.minimalest.api.domain.publishing.exception.UserAlreadyHasBlogException;
import kr.minimalest.api.domain.access.exception.AuthenticateUserException;
import kr.minimalest.api.domain.access.exception.EmailDuplicatedException;
import kr.minimalest.api.domain.access.exception.InvalidRefreshToken;
import kr.minimalest.api.web.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        log.error("예상치 못한 오류 {}: {}", request.getRequestURI(), e.getMessage(), e);
        return createErrorResponse(500, "서버 오류", "요청을 처리하는 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        return createErrorResponse(404, "찾을 수 없음", "해당 리소스가 존재하지 않습니다.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleReqMethodNotSupException(HttpRequestMethodNotSupportedException e) {
        String supportedMethods = e.getSupportedHttpMethods().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", "));

        String detail = "해당 경로의 " + e.getMethod() + "은(는) 허용되지 않습니다. 허용되는 메서드: " + supportedMethods;
        return createErrorResponse(405, "허용되지 않은 메서드", detail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return createErrorResponse(400, "HTTP 메시지 읽기 실패", "올바른 JSON 형식이 아니거나, 요청에 필요한 데이터가 비어있습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return createErrorResponse(400, "입력값 검증 실패", "하나 이상의 필드가 유효하지 않습니다.", Properties.of("errors", errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        String mismatchPropertyName = e.getName();
        String requestPropertyTypeName = e.getParameter().getParameterType().getSimpleName();
        String detail = "요청 타입이 올바르지 않습니다: " + mismatchPropertyName + "의 타입은 " + requestPropertyTypeName + "여야 합니다.";
        return createErrorResponse(400, "올바르지 않은 요청 타입", detail);
    }

    @ExceptionHandler(AuthenticateUserException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationUserException(AuthenticateUserException e) {
        return createErrorResponse(401, "사용자 인증 과정 실패", e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        return createErrorResponse(401, "인증 실패", e.getMessage());
    }

    @ExceptionHandler(InvalidRefreshToken.class)
    public ResponseEntity<ErrorResponse> handleInvalidRefreshToken(InvalidRefreshToken e) {
        return createErrorResponse(401, "토큰 재발급 실패", e.getMessage());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDenied(AuthorizationDeniedException e) {
        return createErrorResponse(403, "인가 거절", e.getMessage());
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicated(EmailDuplicatedException e) {
        return createErrorResponse(409, "이메일 중복", e.getMessage());
    }

    @ExceptionHandler(UserAlreadyHasBlogException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyHasBlog(UserAlreadyHasBlogException e) {
        return createErrorResponse(400, "블로그 생성 실패", e.getMessage());
    }

    @ExceptionHandler(BlogNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBlogNotFound(BlogNotFoundException e) {
        return createErrorResponse(404, "블로그를 찾을 수 없음", e.getMessage());
    }

    @ExceptionHandler(PenNameAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handlePenNameAlreadyExists(PenNameAlreadyExists e) {
        return createErrorResponse(409, "펜네임 중복", e.getMessage());
    }

    @ExceptionHandler(ArticleStateException.class)
    public ResponseEntity<ErrorResponse> handleArticleState(ArticleStateException e) {
        return createErrorResponse(400, "글 상태 오류", e.getMessage());
    }

    @ExceptionHandler(ArticleCompleteFailException.class)
    public ResponseEntity<ErrorResponse> handleArticleCompleteFail(ArticleCompleteFailException e) {
        return createErrorResponse(400, "글 완료 실패", e.getMessage());
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleArticleNotFound(ArticleNotFoundException e) {
        return createErrorResponse(404, "글을 찾을 수 없음", e.getMessage());
    }

    @ExceptionHandler(ArticleAccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleArticleAccessDenied(ArticleAccessDeniedException e) {
        return createErrorResponse(403, "글에 접근할 수 없음", e.getMessage());
    }

    @ExceptionHandler(AuthorNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAuthorNotFound(AuthorNotFoundException e) {
        return createErrorResponse(404, "존재하지 않는 작가", e.getMessage());
    }

    @ExceptionHandler(FileServiceException.class)
    public ResponseEntity<ErrorResponse> handleFileService(FileServiceException e) {
        return createErrorResponse(400, "파일 처리 중 오류", e.getMessage());
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<ErrorResponse> handleFileException(FileException e) {
        int statusCode = e.getOverridableStatusCode();
        return createErrorResponse(statusCode, "파일 관련 오류", e.getMessage());
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(int status, String title, String detail) {
        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(status),
                Title.of(title),
                Detail.of(detail)
        );
        return ResponseEntity.status(status).body(errorResponse);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(int status, String title, String detail, Properties properties) {
        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(status),
                Title.of(title),
                Detail.of(detail),
                properties
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
}
