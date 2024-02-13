package org.example.youth_be.user.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.common.CursorPagingCommon;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.exceptions.YouthDuplicateException;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.common.jwt.TokenProvider;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.domain.UserLinkEntity;
import org.example.youth_be.user.enums.UserRoleEnum;
import org.example.youth_be.user.repository.UserLinkRepository;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.UserAdditionSignupRequest;
import org.example.youth_be.user.service.request.UserSignupRequest;
import org.example.youth_be.user.service.request.LinkRequest;
import org.example.youth_be.user.service.request.UserProfileUpdateRequest;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.user.service.response.UserMyInformation;
import org.example.youth_be.user.service.response.UserMyPage;
import org.example.youth_be.user.service.response.UserProfileResponse;
import org.example.youth_be.user.service.response.UserSignUpResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserLinkRepository userLinkRepository;
    private final ArtworkRepository artworkRepository;
    @Qualifier("accessTokenProvider")
    private final TokenProvider tokenProvider;

    @Transactional
    public Long signup(UserSignupRequest request) {
        UserEntity userEntity = UserEntity.builder()
                .userRole(request.getUserRole())
                .profileImageUrl(request.getProfileImageUrl())
                .activityField(request.getActivityField())
                .activityArea(request.getActivityArea())
                .description(request.getDescription())
                .socialId(request.getSocialId())
                .socialType(request.getSocialType())
                .nickname(request.getNickname())
                .build();
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
        userEntity.updateProfile(request.getProfileImageUrl(), request.getNickname(),
                request.getActivityField(), request.getActivityArea(), request.getDescription());
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

    // 링크 수정
    // 이미지 삭제

    @Transactional
    public void deleteUserLink(Long userId, Long linkId) {
        userLinkRepository.findByIdAndUserId(linkId, userId).orElseThrow(() -> new YouthNotFoundException("링크를 찾을 수 없습니다", null));
        userLinkRepository.deleteById(linkId);
    }

    @Transactional(readOnly = true)
    public PageResponse<ArtworkResponse> getUserArtworks(Long userId, ArtworkMyPageType type, ArtworkPaginationRequest request) {
        Integer size = request.getSize();
        Long cursorId = request.getLastIdxId();

        List<ArtworkResponse> responses = artworkRepository.findByUserAndArtworkType(userId, cursorId, size, type);

        Slice<ArtworkResponse> artworkResponses = CursorPagingCommon.getSlice(responses, size);
        return PageResponse.of(artworkResponses);
    }

    @Transactional(readOnly = true)
    public void checkNicknameDuplicate(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new YouthDuplicateException("닉네임이 중복됩니다.", null);
        }
    }

    public UserMyInformation getMyInformation(TokenClaim tokenClaim) {
        return UserMyInformation.builder().userId(tokenClaim.getUserId()).role(tokenClaim.getUserRole()).build();
    }


    @Transactional
    public UserSignUpResponse signUp(TokenClaim tokenClaim, UserAdditionSignupRequest request) {
        Long userId = tokenClaim.getUserId();

        // 기존 토큰 무효화

        // 정회원 토큰 생성
        String accessToken = tokenProvider.generateToken(userId, UserRoleEnum.REGULAR);

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        userEntity.signUp(request.getProfileImageUrl(), request.getNickname(),
                request.getActivityField(), request.getActivityArea(), request.getDescription());

        // user role 업데이트 및 request 기반 업데이트/ 중복 닉네임 확인
        return UserSignUpResponse.builder().userId(userEntity.getUserId()).role(userEntity.getUserRole()).accessToken(accessToken).build();

    @Transactional(readOnly = true)
    public UserMyPage getMyPage(TokenClaim tokenClaim) {

        Long userId = tokenClaim.getUserId();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        List<UserLinkEntity> userLinkEntities = userLinkRepository.findAllByUserId(userId);
        UserProfileResponse userProfileResponse = UserProfileResponse.of(userEntity, userLinkEntities);

        Long artworkSize = artworkRepository.countByUserId(userId);

        Integer size = 15;
        Long cursorId = 0L;
        if (artworkSize > size) {
            cursorId = artworkSize - size;
        }

        List<ArtworkResponse> responses = artworkRepository.findByUserAndArtworkType(userId, cursorId, size, ArtworkMyPageType.ALL);

        Slice<ArtworkResponse> artworkResponses = CursorPagingCommon.getSlice(responses, size);
        PageResponse<ArtworkResponse> artworkResponse = PageResponse.of(artworkResponses);

        return UserMyPage.builder().userProfileResponse(userProfileResponse).artworkResponsePageResponse(artworkResponse).build();

    }
}
