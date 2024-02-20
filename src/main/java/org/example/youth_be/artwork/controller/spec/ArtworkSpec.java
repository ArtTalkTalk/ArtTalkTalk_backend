package org.example.youth_be.artwork.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.artwork.service.request.ArtworkCreateRequest;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.request.ArtworkUpdateRequest;
import org.example.youth_be.artwork.service.response.ArtworkDetailResponse;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.jwt.TokenClaim;

@Tag(name = ApiTags.ARTWORK)
public interface ArtworkSpec {
    @Operation(description = "작품 업로드 API입니다.")
    Long createArtwork(TokenClaim tokenClaim, ArtworkCreateRequest request);

    @Operation(description = "홈화면 전체 피드 조회 API입니다.")
    PageResponse<ArtworkResponse> getArtworks(ArtworkPaginationRequest request);

    @Operation(description = "유저가 팔로잉한 유저들의 피드 조회 API입니다.")
    PageResponse<ArtworkResponse> getArtworksFollowing(TokenClaim tokenClaim, ArtworkPaginationRequest request);


    @Operation(description = "작품 상세 조회 API입니다.")
    ArtworkDetailResponse getArtwork(Long artworkId, TokenClaim claim);

    @Operation(description = "작품 수정 API입니다.")
    void updateArtwork(TokenClaim tokenClaim, Long artworkId, ArtworkUpdateRequest request);
}
