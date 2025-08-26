package kr.minimalest.api.web;

import kr.minimalest.api.domain.user.UserId;
import kr.minimalest.api.infrastructure.security.JwtUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class WithMockJwtUserSecurityContextFactory implements WithSecurityContextFactory<WithMockJwtUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockJwtUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserId userId = UserId.of(UUID.fromString(annotation.userId()));
        Collection<GrantedAuthority> authorities = Arrays.stream(annotation.authorities())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        JwtUserDetails userDetails = new JwtUserDetails(userId, authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities
        );

        context.setAuthentication(authentication);
        return context;
    }
}
