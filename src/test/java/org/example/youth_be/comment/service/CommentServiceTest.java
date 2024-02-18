package org.example.youth_be.comment.service;

import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.comment.domain.CommentEntity;
import org.example.youth_be.comment.repository.CommentRepository;
import org.example.youth_be.comment.service.request.CreateArtworkCommentRequest;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.fixture.ArtworkEntityFixture;
import org.example.youth_be.fixture.CommentEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class CommentServiceTest {
    private static final CommentRepository commentRepository = mock(CommentRepository.class);
    private static final ArtworkRepository artworkRepository = mock(ArtworkRepository.class);
    private static final CommentService commentService = new CommentService(artworkRepository, commentRepository);

    @DisplayName("createArtworkComment 메서드는")
    static class CreateArtworkCommentTest {
        @Test
        void 작품의_댓글_수를_1만큼_증가시키고_댓글_ID를_반환한다() {
            // given
            Long artworkId = 99L;
            Long userId = 10L;
            ArtworkEntity artworkEntity = ArtworkEntityFixture.validCommentCountAny(artworkId);
            Long expectCommentCount = artworkEntity.getCommentCount() + 1;
            CommentEntity commentEntity = CommentEntityFixture.validIdAny();
            TokenClaim claim = new TokenClaim(userId, null, null, null, null);

            given(artworkRepository.findById(any())).willReturn(Optional.of(artworkEntity));
            given(commentRepository.save(any())).willReturn(commentEntity);
            CreateArtworkCommentRequest request = new CreateArtworkCommentRequest("댓글의 내용");

            // when
            Long commentId = commentService.createArtworkComment(claim, artworkId, request);

            // then
            assertThat(commentId).isEqualTo(commentEntity.getCommentId());
            assertThat(artworkEntity.getCommentCount()).isEqualTo(expectCommentCount);
        }
    }

    @DisplayName("deleteArtworkComment 메서드는")
    static class DeleteArtworkCommentTest {
        @Test
        void 댓글의_userId가_TokenClaim의_userId와_다르면_YouthBadRequestException을_던진다() {
            // given
            Long userId = 99L;
            CommentEntity commentEntity = CommentEntityFixture.validUserIdAny(userId - 10);
            given(commentRepository.findById(any())).willReturn(Optional.of(commentEntity));
            TokenClaim claim = new TokenClaim(userId, null, null, null, null);

            // when, then
            assertThrows(YouthBadRequestException.class, () -> {
                commentService.deleteArtworkComment(claim, 10L, commentEntity.getCommentId());
            });
        }

        @Test
        void 댓글을_삭제하면_작품의_댓글개수는_1만큼_감소하고_commentRepository_delete메서드를_호출한다() {
            // given
            Long userId = 99L;
            TokenClaim claim = new TokenClaim(userId, null, null, null, null);
            CommentEntity commentEntity = CommentEntityFixture.validUserIdAny(userId);
            ArtworkEntity artworkEntity = ArtworkEntityFixture.validCommentCountAny(10L);
            Long expectCommentCount = artworkEntity.getCommentCount() - 1;

            given(commentRepository.findById(any())).willReturn(Optional.of(commentEntity));
            given(artworkRepository.findById(any())).willReturn(Optional.of(artworkEntity));

            // when
            commentService.deleteArtworkComment(claim, 10L, 10L);

            // then
            assertThat(artworkEntity.getCommentCount()).isEqualTo(expectCommentCount);
            verify(commentRepository, times(1)).delete(any());
        }
    }
}
