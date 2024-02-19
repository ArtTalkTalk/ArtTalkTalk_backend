package org.example.youth_be.follow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.follow.domain.FollowEntity;
import org.example.youth_be.follow.repository.FollowRepository;
import org.example.youth_be.follow.service.request.CreateFollowRequest;
import org.example.youth_be.follow.service.response.CreateFollowResponse;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public CreateFollowResponse createFollow(Long senderId, CreateFollowRequest request) {
        UserEntity followUser = userRepository.findById(request.getReceiverId()).orElseThrow(() -> new YouthNotFoundException("팔로우할 유저를 찾을 수 없습니다.", null));
        FollowEntity followEntity = FollowEntity.builder()
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .build();
        followRepository.save(followEntity);
        followUser.increaseFollowCount();
        logger.info("createFollow - userId: {}, follow count 증가", senderId);
        return new CreateFollowResponse(followEntity.getFollowId());
    }

    @Transactional
    public void deleteFollow(Long senderId, Long receiverId, Long followId) {
        UserEntity followUser = userRepository.findById(receiverId).orElseThrow(() -> new YouthNotFoundException("팔로우한 유저를 찾을 수 없습니다.", null));
        FollowEntity followEntity = followRepository.findById(followId)
                .orElseThrow(() -> new YouthNotFoundException("해당 ID의 팔로우를 찾을 수 없습니다.", null));
        followUser.decreaseFollowCount();
        followRepository.delete(followEntity);
        logger.info("deleteFollow - userId: {}, follow count 감소", receiverId);
    }
}
