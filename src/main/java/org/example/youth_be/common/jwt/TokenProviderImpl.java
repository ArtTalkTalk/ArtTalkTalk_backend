package org.example.youth_be.common.jwt;

import org.example.youth_be.common.jwt.dto.ParsedTokenInfo;
import org.springframework.stereotype.Service;

@Service
public class TokenProviderImpl implements TokenProvider{
    @Override
    public ParsedTokenInfo parseToken(String token) {
        return null;
    }
}
