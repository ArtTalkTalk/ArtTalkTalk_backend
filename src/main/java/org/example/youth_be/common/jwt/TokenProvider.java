package org.example.youth_be.common.jwt;

import org.example.youth_be.common.jwt.dto.ParsedTokenInfo;

public interface TokenProvider {

    ParsedTokenInfo parseToken(String token);
}
