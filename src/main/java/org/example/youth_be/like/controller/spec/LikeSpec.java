package org.example.youth_be.like.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.like.service.CreateLikeResponse;

@Tag(name = ApiTags.LIKE)
public interface LikeSpec {

    @Operation(description = "작품 좋아요 생성 API입니다.")
    CreateLikeResponse createArtworkLike(Long artworkId, TokenClaim tokenClaim);

    @Operation(description = "작품 좋아요 삭제 API입니다.")
    void deleteArtworkLike(Long artworkId, Long likeId, TokenClaim tokenClaim);
}
