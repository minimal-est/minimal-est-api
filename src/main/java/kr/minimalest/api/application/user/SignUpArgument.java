package kr.minimalest.api.application.user;

import org.springframework.util.Assert;

public record SignUpArgument(String email, String password) {

    public SignUpArgument {
        Assert.hasText(email, "email은 공백일 수 없습니다.");
        Assert.hasText(password, "password는 공백일 수 없습니다.");
    }

    public static SignUpArgument of(String email, String password) {
        return new SignUpArgument(email, password);
    }
}
