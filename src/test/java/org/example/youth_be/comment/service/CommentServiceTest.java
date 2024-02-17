package org.example.youth_be.comment.service;

import org.example.youth_be.artwork.domain.ArtworkEntity;
import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.comment.domain.CommentEntity;
import org.example.youth_be.comment.repository.CommentRepository;
import org.example.youth_be.comment.service.request.CreateArtworkCommentRequest;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.fixture.ArtworkFixture;
import org.example.youth_be.fixture.CommentFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
            ArtworkEntity artworkEntity = ArtworkFixture.validCommentCountAny(artworkId);
            Long expectCommentCount = artworkEntity.getCommentCount() + 1;
            CommentEntity commentEntity = CommentFixture.validIdAny();
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
}
