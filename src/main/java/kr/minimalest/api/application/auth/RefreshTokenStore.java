package kr.minimalest.api.application.auth;

import kr.minimalest.api.domain.user.UserId;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RefreshTokenStore {

    void put(UserId userId, JwtToken refreshToken);

    Optional<JwtToken> find(UserId userId);

    void remove(UserId userId);
}
