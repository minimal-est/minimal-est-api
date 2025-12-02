package kr.minimalest.api.application.user;

import kr.minimalest.api.application.common.annotation.Business;
import kr.minimalest.api.domain.access.RefreshTokenStore;
import kr.minimalest.api.domain.access.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Business
@RequiredArgsConstructor
public class Logout {

    private final RefreshTokenStore refreshTokenStore;

    /**
     * 사용자의 Refresh Token을 삭제합니다.
     */
    @Transactional
    public void exec(LogoutArgument argument) {
        refreshTokenStore.remove(argument.userId());
    }
}
