package kr.minimalest.api.infrastructure.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtProperties(
        String issuer,
        String secretKey,
        long accessValidityInSeconds,
        long refreshValidityInSeconds
) { }
