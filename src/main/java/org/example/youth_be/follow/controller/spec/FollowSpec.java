package org.example.youth_be.follow.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.follow.service.request.CreateFollowRequest;
import org.example.youth_be.follow.service.response.CreateFollowResponse;

@Tag(name = ApiTags.FOLLOW)
public interface FollowSpec {
    @Operation(description = "팔로우 생성 API")
    CreateFollowResponse createFollow(
            TokenClaim claim,
            CreateFollowRequest request);

    @Operation(description = "팔로우 제거 API")
    void deleteFollow(
            TokenClaim claim,
            Long userId,
            Long followId
    );
}
