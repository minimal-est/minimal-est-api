package kr.minimalest.api.domain.user.event;

import kr.minimalest.api.domain.DomainEvent;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.UserId;
import lombok.Getter;
import org.springframework.util.Assert;

@Getter
public class SignedUpEvent extends DomainEvent {
    private final UserId userId;
    private final Email email;

    private SignedUpEvent(UserId userId, Email email) {
        super();
        Assert.notNull(userId, "userId는 null일 수 없습니다.");
        Assert.notNull(email, "email은 null일 수 없습니다.");
        this.userId = userId;
        this.email = email;
    }

    public static SignedUpEvent of(UserId userId, Email email) {
        return new SignedUpEvent(userId, email);
    }
}
