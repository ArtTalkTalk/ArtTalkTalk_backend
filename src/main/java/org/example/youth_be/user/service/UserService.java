package org.example.youth_be.user.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.DevUserProfileCreateRequest;
import org.example.youth_be.user.service.request.UserProfileUpdateRequest;
import org.example.youth_be.user.service.response.UserProfileDto;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    final int COUNT_PER_SCROLL = 15;

    private final UserRepository userRepository;
    private final ArtworkRepository artworkRepository;

    /**
     * 개발용입니다.
     */
    @Transactional
    public void createUserForDev(DevUserProfileCreateRequest request) {
        UserEntity userEntity = UserEntity.builder()
                .profileImageUrl(request.getProfileImageUrl())
                .major(request.getMajor())
                .userRole(request.getUserRole())
                .description(request.getDescription())
                .link(request.getLink())
                .followerCount(request.getFollowerCount())
                .socialId(request.getSocialId())
                .socialType(request.getSocialType())
                .totalLikeCount(request.getTotalLikeCount())
                .nickname(request.getNickname())
                .build();
        userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        return UserProfileDto.of(userEntity);
    }

    @Transactional
    public void updateUserProfile(Long userId, UserProfileUpdateRequest request) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        userEntity.updateProfile(request.getProfileImageUrl(), request.getNickname(), request.getMajor(), request.getDescription(), request.getLink());
    }

    @Transactional(readOnly = true)
    public PageResponse<ArtworkEntity> getUserArtworks(Long userId, Long artworkId) {
        PageRequest pageable = PageRequest.of(0, COUNT_PER_SCROLL);
        Slice<ArtworkEntity> artworks = getArtwork(userId, artworkId, pageable);

        Long lastArtworkId = null;
        if (!artworks.isEmpty()) {
            List<ArtworkEntity> content = artworks.getContent();
            lastArtworkId = content.get(content.size() - 1).getArtworkId(); // 마지막 artwork의 ID
        }

        return PageResponse.of(artworks, lastArtworkId);
    }

    private Slice<ArtworkEntity> getArtwork(Long userId, Long artworkId, Pageable page) {

        if (artworkId == null) {
            // 첫 번째 페이지 요청 시 가장 최신의 데이터부터 시작
            return this.artworkRepository.findAllByUserIdOrderByArtworkIdDesc(userId, page);
        } else {
            // 커서 기반 페이징: 주어진 artworkId보다 작은 데이터 조회
            return this.artworkRepository.findByUserIdAndArtworkIdLessThanOrderByArtworkIdDesc(userId, artworkId, page);
        }
    }
}
