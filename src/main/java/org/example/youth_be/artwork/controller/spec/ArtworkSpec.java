package org.example.youth_be.artwork.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.artwork.enums.ArtworkFeedType;
import org.example.youth_be.artwork.service.request.ArtworkCreateRequest;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.request.ArtworkUpdateRequest;
import org.example.youth_be.artwork.service.request.DevArtworkCreateRequest;
import org.example.youth_be.artwork.service.response.ArtworkDetailResponse;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.jwt.TokenClaim;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = ApiTags.ARTWORK)
public interface ArtworkSpec {
    @Operation(description = "작품 업로드 API입니다.")
    Long createArtwork(TokenClaim tokenClaim, ArtworkCreateRequest request);

    @Operation(description = "홈화면 피드 조회 API입니다.")
    PageResponse<ArtworkResponse> getArtworks(Long userId, ArtworkFeedType type, ArtworkPaginationRequest request);

    @Operation(description = "작품 상세 조회 API입니다.")
    ArtworkDetailResponse getArtwork(Long artworkId);

    @Operation(description = "작품 수정 API입니다.")
    void updateArtwork(TokenClaim tokenClaim, Long artworkId, ArtworkUpdateRequest request);
}
