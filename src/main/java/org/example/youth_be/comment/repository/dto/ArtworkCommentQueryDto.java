package org.example.youth_be.comment.repository.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ArtworkCommentQueryDto {
    private Long commentId;
    private String profileImage;
    private String nickname;
    private LocalDateTime createdAt;
    private String contents;

    @QueryProjection
    public ArtworkCommentQueryDto(Long commentId, String profileImage, String nickname, LocalDateTime createdAt, String contents) {
        this.commentId = commentId;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.contents = contents;
    }
}
