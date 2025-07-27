package kr.minimalest.api.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import kr.minimalest.api.web.ErrorResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public abstract class AbstractErrorResponseHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    protected void writeErrorResponse(HttpServletResponse response, ErrorResponse errorResponse, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(errorResponseJson);
    }
}
