package kr.minimalest.api.application.auth;

import kr.minimalest.api.domain.user.UserUUID;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RefreshTokenStore {

    void put(UserUUID userUUID, JwtToken refreshToken);

    Optional<JwtToken> find(UserUUID userUUID);

    void remove(UserUUID userUUID);
}
