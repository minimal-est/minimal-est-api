package kr.minimalest.api.infrastructure.security;

import kr.minimalest.api.application.UserAuthenticator;
import kr.minimalest.api.application.exception.AuthenticateUserException;
import kr.minimalest.api.domain.user.Email;
import kr.minimalest.api.domain.user.Password;
import kr.minimalest.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
