package org.example.youth_be.user.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.repository.ArtworkRepositoryCustom;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.domain.UserLinkEntity;
import org.example.youth_be.user.enums.ArtworkType;
import org.example.youth_be.user.repository.UserLinkRepository;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.DevUserProfileCreateRequest;
import org.example.youth_be.user.service.request.LinkRequest;
import org.example.youth_be.user.service.request.UserProfileUpdateRequest;
import org.example.youth_be.user.service.response.UserArtworkResponse;
import org.example.youth_be.user.service.response.UserProfileResponse;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserLinkRepository userLinkRepository;
    private final ArtworkRepositoryCustom artworkRepositoryCustom;


    /**
     * 개발용입니다.
     */
    @Transactional
    public Long createUserForDev(DevUserProfileCreateRequest request) {
        UserEntity userEntity = UserEntity.builder()
                .profileImageUrl(request.getProfileImageUrl())
                .major(request.getMajor())
                .description(request.getDescription())
                .followerCount(request.getFollowerCount())
                .socialId(request.getSocialId())
                .socialType(request.getSocialType())
                .totalLikeCount(request.getTotalLikeCount())
                .nickname(request.getNickname())
                .build();
        userRepository.save(userEntity);

        List<UserLinkEntity> userLinkEntities = request.getLinkRequestList().stream().map(linkRequest ->
                UserLinkEntity.builder()
                        .userId(userEntity.getUserId())
                        .title(linkRequest.getTitle())
                        .linkUrl(linkRequest.getUrl()).build()).toList();
        userLinkRepository.saveAll(userLinkEntities);
        return userEntity.getUserId();
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        List<UserLinkEntity> userLinkEntities = userLinkRepository.findAllByUserId(userId);
        return UserProfileResponse.of(userEntity, userLinkEntities);
    }

    @Transactional
    public void updateUserProfile(Long userId, UserProfileUpdateRequest request) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        userEntity.updateProfile(request.getProfileImageUrl(), request.getNickname(), request.getMajor(), request.getDescription());
    }

    @Transactional
    public Long createUserLink(Long userId, LinkRequest request) {
        userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        UserLinkEntity newUserLinkEntity = UserLinkEntity.builder()
                .userId(userId)
                .title(request.getTitle())
                .linkUrl(request.getUrl())
                .build();
        userLinkRepository.save(newUserLinkEntity);
        return newUserLinkEntity.getUserId();
    }

    @Transactional
    public void deleteUserLink(Long userId, Long linkId) {
        userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        userLinkRepository.deleteById(linkId);
    }

    @Transactional(readOnly = true)
    public PageResponse<UserArtworkResponse> getUserArtworks(Long userId, ArtworkType type, ArtworkPaginationRequest request) {
        Integer size = request.getSize();
        Long cursorId = request.getLastIdxId();

        Slice<UserArtworkResponse> response = null;
        if (type == ArtworkType.ALL){
            response = artworkRepositoryCustom.findAllByCondition(userId, cursorId, size);
        } else if (type == ArtworkType.SELLING) {
            response = artworkRepositoryCustom.findSellingsByCondition(userId, cursorId, size);
        } else {
            response = artworkRepositoryCustom.findLikedByCondition(userId, cursorId, size);
        }

        return PageResponse.of(response);
    }
}
