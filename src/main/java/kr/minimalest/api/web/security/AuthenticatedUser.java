package kr.minimalest.api.web.security;

import kr.minimalest.api.domain.access.UserId;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인증된 사용자의 UserId를 주입받기 위한 커스텀 어노테이션
 * 사용 예: @AuthenticatedUser UserId userId
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@AuthenticationPrincipal(expression = "userId")
public @interface AuthenticatedUser {
}
