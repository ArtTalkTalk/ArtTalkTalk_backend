package org.example.youth_be.follow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.aop.TransactionalDistributedLock;
import org.example.youth_be.common.enums.LockUsageType;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.follow.domain.FollowEntity;
import org.example.youth_be.follow.repository.FollowRepository;
import org.example.youth_be.follow.service.response.CreateFollowResponse;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @TransactionalDistributedLock(key = "#claim.getUserId()", usage = LockUsageType.FOLLOW)
    public CreateFollowResponse createFollow(TokenClaim claim, Long senderId, Long receiverId) {
        if (claim.getUserId() != senderId) {
            throw new YouthBadRequestException("유저의 토큰이 일치하지 않습니다.", null);
        }
        if (followRepository.findBySenderIdAndReceiverId(senderId, receiverId).isPresent()) {
            throw new YouthBadRequestException("이미 팔로우한 유저입니다.", null);
        }
        UserEntity followUser = userRepository.findById(receiverId).orElseThrow(() -> new YouthNotFoundException("팔로우할 유저를 찾을 수 없습니다.", null));
        FollowEntity followEntity = FollowEntity.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .build();
        followRepository.save(followEntity);
        followUser.increaseFollowCount();
        logger.info("createFollow - userId: {}, follow count 증가", senderId);
        return new CreateFollowResponse(followEntity.getFollowId());
    }

    @Transactional
    public void deleteFollow(Long senderId, Long receiverId, Long followId) {
        UserEntity followUser = userRepository.findById(receiverId).orElseThrow(() -> new YouthNotFoundException("팔로우한 유저를 찾을 수 없습니다.", null));
        FollowEntity followEntity = followRepository.findByFollowIdAndSenderIdAndReceiverId(followId, senderId, receiverId)
                .orElseThrow(() -> new YouthNotFoundException("팔로우 내역을 찾을 수 없습니다. 팔로우했는지 확인해주세요", null));
        followUser.decreaseFollowCount();
        followRepository.delete(followEntity);
        logger.info("deleteFollow - userId: {}, follow count 감소", receiverId);
    }
}
