package org.example.youth_be.artwork.repository;

import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.springframework.data.domain.Slice;

public interface ArtworkRepositoryCustom {

    Slice<ArtworkResponse> findByUserAndArtworkType(Long userId, Long cursorId, Integer size, String type);

    Slice<ArtworkResponse> findByFeedType(Long userId, Long cursorId, Integer size, String type);
}
