package org.example.youth_be.comment.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArtworkCommentQueryDto {
    private Long commentId;
    private Long userId;
    private String profileImage;
    private String nickname;
    private LocalDateTime createdAt;
    private String contents;

    @QueryProjection
    public ArtworkCommentQueryDto(Long commentId, Long userId, String profileImage, String nickname, LocalDateTime createdAt, String contents) {
        this.commentId = commentId;
        this.userId = userId;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.contents = contents;
    }
}
