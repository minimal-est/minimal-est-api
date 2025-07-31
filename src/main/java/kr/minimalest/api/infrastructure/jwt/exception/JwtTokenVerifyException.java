package kr.minimalest.api.infrastructure.jwt.exception;

public class JwtTokenVerifyException extends RuntimeException{

    public JwtTokenVerifyException() {
        super();
    }

    public JwtTokenVerifyException(String message) {
        super(message);
    }

    public JwtTokenVerifyException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtTokenVerifyException(Throwable cause) {
        super(cause);
    }
}
