package org.example.youth_be.comment.service;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.comment.domain.CommentEntity;
import org.example.youth_be.comment.repository.CommentRepository;
import org.example.youth_be.comment.repository.dto.ArtworkCommentQueryDto;
import org.example.youth_be.comment.service.request.CreateArtworkCommentRequest;
import org.example.youth_be.comment.service.request.UpdateArtworkCommentRequest;
import org.example.youth_be.comment.service.response.CommentResponse;
import org.example.youth_be.common.PageParam;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.aop.TransactionalDistributedLock;
import org.example.youth_be.common.enums.LockUsageType;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ArtworkRepository artworkRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getArtworkComments(TokenClaim claim, Long artworkId, PageParam pageParam) {
        Slice<ArtworkCommentQueryDto> allArtworkComments = commentRepository.findAllArtworkComments(artworkId, pageParam.getLastIdxId(), pageParam.getSize());
        return PageResponse.of(allArtworkComments
                        .getContent()
                        .stream()
                        .map(dto -> CommentResponse.of(dto, claim))
                        .collect(Collectors.toList()),
                allArtworkComments.hasNext());
    }

    @TransactionalDistributedLock(key = "#claim.getUserId()", usage = LockUsageType.COMMENT)
    public Long createArtworkComment(TokenClaim claim, Long artworkId, CreateArtworkCommentRequest request) {
        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId).orElseThrow(() -> new YouthNotFoundException("작품을 찾을 수 없습니다.", null));
        CommentEntity newCommentEntity = CommentEntity.builder()
                .contents(request.getContents())
                .artworkId(artworkId)
                .userId(claim.getUserId())
                .build();
        Long commentId = commentRepository.save(newCommentEntity).getCommentId();
        artworkEntity.increaseCommentCount();
        logger.info("create comment ok - userId: {}, artworkId: {}, commentId: {}", claim.getUserId(), artworkId, commentId);
        return commentId;
    }

    @Transactional
    public void deleteArtworkComment(TokenClaim claim, Long artworkId, Long commentId) {
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new YouthNotFoundException("댓글을 찾을 수 없습니다.", null));
        validateCommentOwner(commentEntity, claim.getUserId());

        ArtworkEntity artworkEntity = artworkRepository.findById(artworkId).orElseThrow(() -> new YouthNotFoundException("작품을 찾을 수 없습니다.", null));
        commentRepository.delete(commentEntity);
        artworkEntity.decreaseCommentCount();
        logger.info("delete comment ok - userId: {}, artworkId: {}, commentId: {}", claim.getUserId(), artworkId, commentId);
    }

    @Transactional
    public void updateArtworkComment(TokenClaim claim, Long artworkId, Long commentId, UpdateArtworkCommentRequest request) {
        CommentEntity commentEntity = commentRepository.findById(commentId).orElseThrow(() -> new YouthNotFoundException("댓글을 찾을 수 없습니다.", null));
        validateCommentOwner(commentEntity, claim.getUserId());

        commentEntity.updateContents(request.getContents());
        logger.info("update comment ok - userId: {}, artworkId: {}, contents: {}", claim.getUserId(), artworkId, request.getContents());
    }

    private void validateCommentOwner(CommentEntity commentEntity, Long userId) {
        if (commentEntity.isOwner(userId)) {
            return;
        }
        throw new YouthBadRequestException("댓글 삭제는 작성자만 가능합니다.", null);
    }
}
