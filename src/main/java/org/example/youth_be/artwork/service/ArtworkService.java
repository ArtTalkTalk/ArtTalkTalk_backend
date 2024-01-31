package org.example.youth_be.artwork.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.artwork.service.request.DevArtworkCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return artworkRepository.save(entity).getArtworkId();
    }
}
