package kr.minimalest.api.infrastructure.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.minimalest.api.web.Detail;
import kr.minimalest.api.web.ErrorResponse;
import kr.minimalest.api.web.Status;
import kr.minimalest.api.web.Title;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint extends AbstractErrorResponseHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorResponse errorResponse = ErrorResponse.of(
                Status.of(401),
                Title.of("인증 실패"),
                Detail.of(authException.getMessage())
        );

        writeErrorResponse(response, errorResponse, 401);
    }
}
