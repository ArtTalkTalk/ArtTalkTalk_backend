package org.example.youth_be.artwork.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.request.DevArtworkCreateRequest;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.common.PageResponse;

@Tag(name = ApiTags.ARTWORK)
public interface ArtworkSpec {
    @Operation(description = "mock 데이터 용 작품 업로드 API입니다.")
    Long createArtworkForDev(DevArtworkCreateRequest request);

    @Operation(description = "홈화면 피드 조회 API입니다.")
    PageResponse<ArtworkResponse> getArtworks(Long userId, String type, ArtworkPaginationRequest request);
}
