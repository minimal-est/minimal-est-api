package kr.minimalest.api.application.user;

import kr.minimalest.api.domain.user.UserId;
import org.springframework.util.Assert;

public record SignUpResult(UserId userId) {

    public SignUpResult {
        Assert.notNull(userId, "userId는 null이 될 수 없습니다.");
    }

    public static SignUpResult of(UserId userId) {
        return new SignUpResult(userId);
    }
}
