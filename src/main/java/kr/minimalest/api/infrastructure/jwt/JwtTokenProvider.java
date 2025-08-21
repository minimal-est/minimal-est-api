package kr.minimalest.api.infrastructure.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.minimalest.api.domain.user.*;
import kr.minimalest.api.application.exception.TokenVerificationException;
import kr.minimalest.api.domain.user.service.TokenProvider;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class JwtTokenProvider implements TokenProvider {
    private final String CLAIM_ROLES = "roles";

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.secretKey());
        this.verifier = JWT.require(algorithm).withIssuer(jwtProperties.issuer()).build();
    }

    @Override
    public TokenPayload verify(Token token) {
        DecodedJWT decodedJWT = verifyAndGetDecodedJWT(token);
        return serializeToJwtPayloadFrom(decodedJWT);
    }

    private DecodedJWT verifyAndGetDecodedJWT(Token token) {
        try {
            return verifier.verify(token.value());
        } catch (Exception e) {
            throw new TokenVerificationException("토큰을 증명하고 해석하는 데 실패했습니다!", e);
        }
    }

    private TokenPayload serializeToJwtPayloadFrom(DecodedJWT decodedJWT) {
        try {
            UserId userId = UserId.of(UUID.fromString(decodedJWT.getSubject()));
            List<String> roleStrings = decodedJWT.getClaim(CLAIM_ROLES).asList(String.class);

            if (roleStrings == null || roleStrings.isEmpty()) {
                throw new IllegalArgumentException("Roles 클레임이 없습니다!");
            }

            List<RoleType> roleTypes = roleStrings.stream()
                    .map(RoleType::valueOf)
                    .toList();

            Instant issuedAt = decodedJWT.getIssuedAt().toInstant();
            Instant expiresAt = decodedJWT.getExpiresAt().toInstant();

            return TokenPayload.of(userId, roleTypes, issuedAt, expiresAt);
        } catch (Exception e) {
            throw new IllegalStateException("JwtTokenPayload 역직렬화에 실패했습니다!", e);
        }
    }

    @Override
    public Token generateAccessToken(UserId userId, List<RoleType> roleTypes) {
        return generateToken(userId, roleTypes, TokenValidityInMills.ofSeconds(jwtProperties.accessValidityInSeconds()));
    }

    @Override
    public Token generateRefreshToken(UserId userId, List<RoleType> roleTypes) {
        return generateToken(userId, roleTypes, TokenValidityInMills.ofSeconds(jwtProperties.refreshValidityInSeconds()));
    }

    @Override
    public Token generateToken(UserId userId, List<RoleType> roleTypes, TokenValidityInMills tokenValidityInMills) {
        try {
            Instant now = Instant.now();
            return Token.of(
                    JWT.create()
                    .withIssuer(jwtProperties.issuer())
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(now.plusMillis(tokenValidityInMills.value())))
                    .withSubject(userId.id().toString())
                    .withClaim(CLAIM_ROLES, roleTypes.stream().map(Enum::name).toList())
                    .sign(algorithm));
        } catch (Exception e) {
            throw new IllegalStateException("토큰을 생성하는 데 실패했습니다!", e);
        }
    }

    @Override
    public TokenValidityInMills getAccessValidityInMills() {
        return TokenValidityInMills.ofSeconds(jwtProperties.accessValidityInSeconds());
    }

    @Override
    public TokenValidityInMills getRefreshValidityInMills() {
        return TokenValidityInMills.ofSeconds(jwtProperties.refreshValidityInSeconds());
    }
}
