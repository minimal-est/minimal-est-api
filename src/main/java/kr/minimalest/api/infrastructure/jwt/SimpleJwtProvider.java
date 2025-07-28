package kr.minimalest.api.infrastructure.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.minimalest.api.application.auth.JwtTokenPayload;
import kr.minimalest.api.application.auth.JwtProvider;
import kr.minimalest.api.application.auth.JwtToken;
import kr.minimalest.api.application.auth.JwtTokenValidityInMills;
import kr.minimalest.api.domain.user.RoleType;
import kr.minimalest.api.domain.user.UserUUID;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class SimpleJwtProvider implements JwtProvider {

    private final String CLAIM_ROLES = "roles";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JwtProperties jwtProperties;
    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public SimpleJwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.algorithm = Algorithm.HMAC256(jwtProperties.secretKey());
        this.verifier = JWT.require(algorithm).withIssuer(jwtProperties.issuer()).build();
    }

    @Override
    public JwtTokenPayload verify(JwtToken jwtToken) {
        DecodedJWT decodedJWT = verifier.verify(jwtToken.value());
        return serializeToJwtPayloadFrom(decodedJWT);
    }

    private JwtTokenPayload serializeToJwtPayloadFrom(DecodedJWT decodedJWT) {
        try {
            UserUUID userUUID = UserUUID.of(decodedJWT.getSubject());
            List<RoleType> roleTypes = objectMapper.readValue(
                    decodedJWT.getClaim(CLAIM_ROLES).asString(),
                    new TypeReference<List<RoleType>>() {
                    }
            );
            Instant issuedAt = decodedJWT.getIssuedAt().toInstant();
            Instant expiresAt = decodedJWT.getExpiresAt().toInstant();
            return JwtTokenPayload.of(userUUID, roleTypes, issuedAt, expiresAt);
        } catch (Exception e) {
            throw new IllegalStateException("JwtPayload 역직렬화에 실패했습니다!", e);
        }
    }

    @Override
    public JwtToken generateAccessToken(UserUUID userUUID, List<RoleType> roleTypes) {
        return generateToken(userUUID, roleTypes, JwtTokenValidityInMills.ofSeconds(jwtProperties.accessValidityInSeconds()));
    }

    @Override
    public JwtToken generateRefreshToken(UserUUID userUUID, List<RoleType> roleTypes) {
        return generateToken(userUUID, roleTypes, JwtTokenValidityInMills.ofSeconds(jwtProperties.refreshValidityInSeconds()));
    }

    @Override
    public JwtToken generateToken(UserUUID userUUID, List<RoleType> roleTypes, JwtTokenValidityInMills jwtTokenValidityInMills) {
        try {
            Instant now = Instant.now();
            return JwtToken.of(JWT.create()
                    .withIssuer(jwtProperties.issuer())
                    .withIssuedAt(Date.from(now))
                    .withExpiresAt(Date.from(now.plusMillis(jwtTokenValidityInMills.value())))
                    .withSubject(userUUID.value())
                    .withClaim(CLAIM_ROLES, objectMapper.writeValueAsString(roleTypes))
                    .sign(algorithm));
        } catch (Exception e) {
            throw new IllegalStateException("토큰을 생성하는 데 실패했습니다!", e);
        }
    }

    @Override
    public JwtTokenValidityInMills getAccessValidityInMills() {
        return JwtTokenValidityInMills.ofSeconds(jwtProperties.accessValidityInSeconds());
    }

    @Override
    public JwtTokenValidityInMills getRefreshValidityInMills() {
        return JwtTokenValidityInMills.ofSeconds(jwtProperties.refreshValidityInSeconds());
    }
}
