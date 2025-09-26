package kr.minimalest.api.infrastructure.security;

import kr.minimalest.api.domain.access.service.UserAuthenticator;
import kr.minimalest.api.domain.access.exception.AuthenticateUserException;
import kr.minimalest.api.domain.access.Email;
import kr.minimalest.api.domain.access.Password;
import kr.minimalest.api.domain.access.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 로그인 과정에만 사용됩니다. 로그인 외에는 사용되지 않습니다.
 * 인증은 Jwt 필터에서 이루어지며, 자동으로 주입됩니다.
 * @see JwtAuthenticationFilter
 */
@Service
@RequiredArgsConstructor
public class SpringSecurityUserAuthenticator implements UserAuthenticator {

    private final AuthenticationManager authenticationManager;

    @Override
    public User authenticate(Email email, Password password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email.value(), password.value());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            return userDetails.getUser();
        } catch (Exception e) {
            throw new AuthenticateUserException("이메일, 비밀번호 인증에 실패했습니다.", e);
        }
    }
}
