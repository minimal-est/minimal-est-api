package kr.minimalest.api.infrastructure.refreshtokenstore;

import kr.minimalest.api.application.auth.JwtToken;
import kr.minimalest.api.application.auth.RefreshTokenStore;
import kr.minimalest.api.domain.user.UserId;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRefreshTokenStore implements RefreshTokenStore {

    private final Map<UserId, JwtToken> store = new ConcurrentHashMap<>();

    @Override
    public void put(UserId userId, JwtToken refreshToken) {
        store.put(userId, refreshToken);
    }

    @Override
    public Optional<JwtToken> find(UserId userId) {
        return Optional.ofNullable(store.getOrDefault(userId, null));
    }

    @Override
    public void remove(UserId userId) {
        store.remove(userId);
    }
}
