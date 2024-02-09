package org.example.youth_be.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParsedTokenInfo {
    private TokenClaim tokenClaim;
    private JwtThrowableType throwableType;
}
