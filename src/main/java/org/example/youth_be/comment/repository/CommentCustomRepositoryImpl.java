package org.example.youth_be.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.comment.repository.dto.ArtworkCommentQueryDto;
import org.example.youth_be.comment.repository.dto.QArtworkCommentQueryDto;
import org.example.youth_be.common.CursorPagingCommon;
import org.springframework.data.domain.Slice;

import java.util.List;

import static org.example.youth_be.comment.domain.QCommentEntity.commentEntity;
import static org.example.youth_be.user.domain.QUserEntity.userEntity;

@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<ArtworkCommentQueryDto> findAllArtworkComments(Long artworkId, Long cursorId, Integer size) {
        List<ArtworkCommentQueryDto> contents = jpaQueryFactory.select(new QArtworkCommentQueryDto(
                                userEntity.profileImageUrl,
                                userEntity.nickname,
                                commentEntity.createdAt,
                                commentEntity.contents
                        )
                )
                .from(commentEntity)
                .join(userEntity).on(userEntity.userId.eq(commentEntity.userId))
                .where(
                        ltLastIdxId(cursorId),
                        commentEntity.artworkId.eq(artworkId)
                )
                .orderBy(commentEntity.commentId.desc())
                .fetch();
        return CursorPagingCommon.getSlice(contents, size);
    }

    private BooleanExpression ltLastIdxId(Long cursorId) {
        if (cursorId == null) {
            return null;
        }
        return commentEntity.commentId.lt(cursorId);
    }
}
