package org.example.youth_be.like.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import org.example.youth_be.common.jwt.TokenClaim;

public interface LikeSpec {

    @Operation(description = "작품 좋아요 생성 API입니다.")
    void createArtworkLike(Long artworkId, TokenClaim tokenClaim);

    @Operation(description = "작품 좋아요 삭제 API입니다.")
    void deleteArtworkLike(Long artworkId, Long likeId, TokenClaim tokenClaim);
}
