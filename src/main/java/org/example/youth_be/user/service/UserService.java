package org.example.youth_be.user.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.domain.UserLinkEntity;
import org.example.youth_be.user.repository.UserLinkRepository;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.DevUserProfileCreateRequest;
import org.example.youth_be.user.service.request.UserProfileUpdateRequest;
import org.example.youth_be.user.service.response.UserProfileResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserLinkRepository userLinkRepository;

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
        List<UserLinkEntity> userLinkEntities = request.getLinkRequestList().stream().map(linkRequest ->
                UserLinkEntity.builder()
                        .userId(userEntity.getUserId())
                        .title(linkRequest.getTitle())
                        .linkUrl(linkRequest.getUrl()).build()).toList();
        userLinkRepository.saveAll(userLinkEntities);
        userRepository.save(userEntity);
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
}
