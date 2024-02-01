package org.example.youth_be.artwork.repository;

import org.example.youth_be.artwork.enums.ArtworkFeedType;
import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ArtworkRepositoryCustom {

    List<ArtworkResponse> findByUserAndArtworkType(Long userId, Long cursorId, Integer size, ArtworkMyPageType type);

    List<ArtworkResponse> findByFeedType(Long userId, Long cursorId, Integer size, ArtworkFeedType type);
}
