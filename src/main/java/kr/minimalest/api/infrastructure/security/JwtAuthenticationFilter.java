package kr.minimalest.api.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.minimalest.api.application.auth.JwtProvider;
import kr.minimalest.api.application.auth.JwtToken;
import kr.minimalest.api.application.auth.JwtTokenPayload;
import kr.minimalest.api.domain.user.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (!StringUtils.hasText(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            try {
                JwtTokenPayload payload = jwtProvider.verify(JwtToken.of(token));

                List<SimpleGrantedAuthority> authorities = payload.roleTypes().stream()
                        .map(RoleType::name)
                        .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                        .toList();

                JwtUserDetails jwtUserDetails = new JwtUserDetails(payload.userUUID(), authorities);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        jwtUserDetails,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                log.debug("인증되지 않은 사용자의 요청: ", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
