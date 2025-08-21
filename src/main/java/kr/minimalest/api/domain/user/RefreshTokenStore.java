package kr.minimalest.api.domain.user;

import java.util.Optional;

public interface RefreshTokenStore {

    void put(UserId userId, Token refreshToken);

    Optional<Token> find(UserId userId);

    void remove(UserId userId);
}
