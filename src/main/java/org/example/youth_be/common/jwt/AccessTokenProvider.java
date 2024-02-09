package org.example.youth_be.common.jwt;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.example.youth_be.user.enums.UserRoleEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Component
@Qualifier("accessTokenProvider")
public class AccessTokenProvider implements TokenProvider {
    private final JwtProperties jwtProperties;
    private final SecretKey key;
    private final JwtParser parser;

    public AccessTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecretKey()));
        this.parser = Jwts.parserBuilder().setSigningKey(key).build();
    }

    @Override
    public TokenClaim getClaim(String token) {
        TokenClaim tokenClaim = TokenClaim.of(parser.parseClaimsJws(token).getBody());
        if (!StringUtils.equals(tokenClaim.getSub(), SUBJECT_ACCESS_TOKEN)) {
            throw new UnsupportedJwtException("토큰의 sub가 일치하지 않습니다.");
        }
        return tokenClaim;
    }

    @Override
    public String generateToken(Long userId, UserRoleEnum userRole) {
        ZoneId KST = ZoneId.of("Asia/Seoul");
        ZonedDateTime issuedAt = ZonedDateTime.now(KST).truncatedTo(ChronoUnit.SECONDS);
        ZonedDateTime expiration = issuedAt.plusDays(jwtProperties.getAccessValidityInDays());
        return Jwts.builder()
                .claim(KEY_USER_ID, userId)
                .claim(KEY_USER_ROLE, userRole)
                .setSubject(SUBJECT_ACCESS_TOKEN)
                .setIssuedAt(Date.from(issuedAt.toInstant()))
                .setExpiration(Date.from(expiration.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
