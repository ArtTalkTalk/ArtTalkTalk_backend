package org.example.youth_be.artwork.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.enums.ArtworkFeedType;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.request.DevArtworkCreateRequest;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.CursorPagingCommon;
import org.example.youth_be.common.PageResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    /**
     * 개발용입니다.
     */
    @Transactional
    public Long createArtworkForDev(DevArtworkCreateRequest request) {
        ArtworkEntity entity = request.toEntity();
        entity.setCount();
        return artworkRepository.save(entity).getArtworkId();
    }

    @Transactional(readOnly = true)
    public PageResponse<ArtworkResponse> getArtworks(Long userId, ArtworkFeedType type, ArtworkPaginationRequest request) {
        Integer size = request.getSize();
        Long cursorId = request.getLastIdxId();

        List<ArtworkResponse> responses = artworkRepository.findByFeedType(userId, cursorId, size, type);
        Slice<ArtworkResponse> artworkResponses = CursorPagingCommon.getSlice(responses, size);
        return PageResponse.of(artworkResponses);
    }
}
