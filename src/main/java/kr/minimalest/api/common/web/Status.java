package kr.minimalest.api.common.web;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.http.HttpStatus;

public record Status(@JsonValue int value) {
    public Status {
        if (value <= 0 || value >= 1000) {
            throw new IllegalArgumentException("Status는 0보다 크고 1000보다 작아야 합니다!");
        }
    }

    public static Status of(int value) {
        return new Status(value);
    }

    public static Status of(HttpStatus httpStatus) {
        return new Status(httpStatus.value());
    }
}
