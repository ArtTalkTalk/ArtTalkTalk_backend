package org.example.youth_be.user.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.DevUserProfileCreateRequest;
import org.example.youth_be.user.service.request.UserProfileUpdateRequest;
import org.example.youth_be.user.service.response.UserProfileDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("해당 ID의 유저를 찾을 수 없습니다."));
        return UserProfileDto.of(userEntity);
    }

    @Transactional
    public void updateUserProfile(Long userId, UserProfileUpdateRequest request){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("해당 ID의 유저를 찾을 수 없습니다."));

        String profileImageUrl = request.getProfileImageUrl().orElse(null);// 추후 기본 프로필 url로 설정
        String major = request.getMajor().orElse(null);
        String description = request.getDescription().orElse(null);
        String link = request.getLink().orElse(null);

        if (profileImageUrl != null) {
            userEntity.updateProfile(profileImageUrl, request.getNickname(), major, description, link);
            return;
        }

        userEntity.updateProfile(request.getNickname(), major, description, link);
    }
}
