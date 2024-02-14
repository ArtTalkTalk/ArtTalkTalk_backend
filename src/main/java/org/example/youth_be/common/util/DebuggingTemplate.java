package org.example.youth_be.common.util;

import org.example.youth_be.common.jwt.TokenClaim;

public class DebuggingTemplate {
    public static String NotAuthorized(Long userId, TokenClaim claim) {
        return """
                    권한이 없는 사용자입니다. userId: %d, tokenUserId: %d
                    """.formatted(userId, claim.getUserId());
    }
}
