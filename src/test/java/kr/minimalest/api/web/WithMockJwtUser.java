package kr.minimalest.api.web;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * JWT인증을 모킹합니다.
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtUserSecurityContextFactory.class)
public @interface WithMockJwtUser {

    String userId() default "123e4567-e89b-12d3-a456-426614174000";

    String[] authorities() default {"ROLE_USER"};

}
