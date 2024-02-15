package org.example.youth_be.user.service;

import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.common.CursorPagingCommon;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.exceptions.YouthDuplicateException;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.domain.UserLinkEntity;
import org.example.youth_be.user.repository.UserLinkRepository;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.*;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.user.service.response.UserMyInformation;
import org.example.youth_be.user.service.response.UserMyPage;
import org.example.youth_be.user.service.response.UserProfileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.youth_be.common.util.DebuggingTemplate.NotAuthorized;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserLinkRepository userLinkRepository;
    private final ArtworkRepository artworkRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserLinkRepository userLinkRepository, ArtworkRepository artworkRepository) {
        this.userRepository = userRepository;
        this.userLinkRepository = userLinkRepository;
        this.artworkRepository = artworkRepository;
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
    public Long createUserLink(Long userId, LinkRequest request, TokenClaim claim) {
        if (claim.isNotAuthorized(userId)) {
            throw new YouthBadRequestException("권한이 없는 사용자입니다.", NotAuthorized(userId, claim));
        }
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
    @Transactional
    public Long updateUserLink(Long userId, Long linkId, UserLinkUpdateRequest request, TokenClaim claim) {
        if (claim.isNotAuthorized(userId)) {
            throw new YouthBadRequestException("권한이 없는 사용자입니다.", NotAuthorized(userId, claim));
        }
        UserLinkEntity userLinkEntity = userLinkRepository.findByIdAndUserId(linkId, userId).orElseThrow(() -> new YouthNotFoundException("링크를 찾을 수 없습니다", null));
        userLinkEntity.updateLink(request.getTitle(), request.getUrl());
        return userLinkEntity.getId();
    }

    @Transactional
    public void deleteUserLink(Long userId, Long linkId, TokenClaim claim) {
        if (claim.isNotAuthorized(userId)) {
            throw new YouthBadRequestException("권한이 없는 사용자입니다.", NotAuthorized(userId, claim));
        }
        userLinkRepository.findByIdAndUserId(linkId, claim.getUserId()).orElseThrow(() -> new YouthNotFoundException("링크를 찾을 수 없습니다", null));
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
