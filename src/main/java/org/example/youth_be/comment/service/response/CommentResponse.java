package org.example.youth_be.comment.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.comment.repository.dto.ArtworkCommentQueryDto;
import org.example.youth_be.common.jwt.TokenClaim;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class CommentResponse {
    @Schema(description = "댓글 ID")
    private Long commentId;
    @Schema(description = "프로필 이미지 url")
    private String profileUrl;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "작성일자")
    private LocalDateTime createdAt;
    @Schema(description = "댓글 내용")
    private String contents;
    @Schema(description = "댓글 작성자 여부")
    private boolean isAuthor;

    public static CommentResponse of (ArtworkCommentQueryDto queryDto, TokenClaim claim) {
        boolean isAuthor = Objects.nonNull(claim) && Objects.equals(claim.getUserId(), queryDto.getUserId());
        return new CommentResponse(
                queryDto.getCommentId(),
                queryDto.getProfileImage(),
                queryDto.getNickname(),
                queryDto.getCreatedAt(),
                queryDto.getContents(),
                isAuthor
        );
    }
}
