package org.example.youth_be.artwork.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.artwork.service.request.DevArtworkCreateRequest;
import org.example.youth_be.common.ApiTags;

@Tag(name = ApiTags.ARTWORK)
public interface ArtworkSpec {
    @Operation(description = "mock 데이터 용 작품 업로드 API입니다.")
    Long createArtworkForDev(DevArtworkCreateRequest request);
}
