package org.example.youth_be.user.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.DevUserProfileCreateRequest;
import org.example.youth_be.user.service.request.UserProfileUpdateRequest;
import org.example.youth_be.user.service.response.UserProfileDto;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

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
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        return UserProfileDto.of(userEntity);
    }

    @Transactional
    public void updateUserProfile(Long userId, UserProfileUpdateRequest request){
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        userEntity.updateProfile(request.getProfileImageUrl(), request.getNickname(), request.getMajor(), request.getDescription(), request.getLink());
    }
}
