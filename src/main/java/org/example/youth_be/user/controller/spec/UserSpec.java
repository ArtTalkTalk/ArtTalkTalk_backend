package org.example.youth_be.user.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.user.service.request.*;
import org.example.youth_be.user.service.response.UserMyInformation;
import org.example.youth_be.user.service.response.UserMyPage;
import org.example.youth_be.user.service.response.UserProfileResponse;

@Tag(name = ApiTags.USER)
public interface UserSpec {
    @Operation(description = "닉네임 중복 체크 API")
    void checkNicknameDuplicate(String nickname);

    @Operation(description = "유저 프로필 조회 API")
    UserProfileResponse getUserProfile(Long userId);

    @Operation(description = "유저 프로필 수정 API")
    void updateUserProfile(Long userId, UserProfileUpdateRequest request);

    @Operation(description = "유저 링크 생성 API")
    Long createUserLink(Long userId, LinkRequest request, TokenClaim claim);

    @Operation(description = "유저 링크 삭제 API")
    void deleteUserLink(Long userId, Long linkId, TokenClaim claim);

    @Operation(description = "유저 링크 수정 API \n\nNote: 수정된 링크 ID를 반환합니다")
    Long updateUserLink(Long userId, Long linkId, UserLinkUpdateRequest request, TokenClaim claim);

    @Operation(description = "유저의 작품 조회 API")
    PageResponse<ArtworkResponse> getUserArtworks(Long userId, ArtworkMyPageType type, ArtworkPaginationRequest request);

    @Operation(description = "유저 Id, Role API")
    UserMyInformation getMyInformation(TokenClaim tokenClaim);

    @Operation(description = "유저 마이페이지 조회 API")
    UserMyPage getMyPage(TokenClaim tokenClaim);
}
