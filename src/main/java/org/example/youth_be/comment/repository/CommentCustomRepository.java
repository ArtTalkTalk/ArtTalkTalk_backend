package org.example.youth_be.comment.repository;

import org.example.youth_be.comment.repository.dto.ArtworkCommentQueryDto;
import org.springframework.data.domain.Slice;

public interface CommentCustomRepository {
    Slice<ArtworkCommentQueryDto> findAllArtworkComments(Long artworkId, Long lastIdxId, Integer size);
}
