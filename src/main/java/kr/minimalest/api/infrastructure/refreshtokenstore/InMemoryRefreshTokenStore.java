package kr.minimalest.api.infrastructure.refreshtokenstore;

import kr.minimalest.api.application.auth.JwtToken;
import kr.minimalest.api.application.auth.RefreshTokenStore;
import kr.minimalest.api.domain.user.UserUUID;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRefreshTokenStore implements RefreshTokenStore {

    private final Map<UserUUID, JwtToken> store = new ConcurrentHashMap<>();

    @Override
    public void put(UserUUID userUUID, JwtToken refreshToken) {
        store.put(userUUID, refreshToken);
    }

    @Override
    public Optional<JwtToken> find(UserUUID userUUID) {
        return Optional.ofNullable(store.getOrDefault(userUUID, null));
    }

    @Override
    public void remove(UserUUID userUUID) {
        store.remove(userUUID);
    }
}
