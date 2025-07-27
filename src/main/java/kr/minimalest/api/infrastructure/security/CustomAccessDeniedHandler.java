package kr.minimalest.api.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.minimalest.api.web.Detail;
import kr.minimalest.api.web.ErrorResponse;
import kr.minimalest.api.web.Status;
import kr.minimalest.api.web.Title;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler extends AbstractErrorResponseHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(401),
                Title.of("권한 없음"),
                Detail.of(accessDeniedException.getMessage())
        );

        writeErrorResponse(response, errorResponse, 401);
    }
}
