package kr.minimalest.api.infrastructure.refreshtokenstore;

import kr.minimalest.api.domain.user.RefreshTokenStore;
import kr.minimalest.api.domain.user.Token;
import kr.minimalest.api.domain.user.UserId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryJwtRefreshTokenStore implements RefreshTokenStore {

    private final Map<UserId, Token> store = new ConcurrentHashMap<>();

    @Override
    public void put(UserId userId, Token refreshToken) {
        store.put(userId, refreshToken);
    }

    @Override
    public Optional<Token> find(UserId userId) {
        return Optional.ofNullable(store.getOrDefault(userId, null));
    }

    @Override
    public void remove(UserId userId) {
        store.remove(userId);
    }
}
