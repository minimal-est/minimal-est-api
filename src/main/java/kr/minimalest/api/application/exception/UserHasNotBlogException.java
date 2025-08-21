package kr.minimalest.api.application.exception;

public class UserHasNotBlogException extends BusinessException{
    public UserHasNotBlogException() {
        super();
    }

    public UserHasNotBlogException(Throwable cause) {
        super(cause);
    }

    public UserHasNotBlogException(String message) {
        super(message);
    }

    public UserHasNotBlogException(String message, Throwable cause) {
        super(message, cause);
    }
}
