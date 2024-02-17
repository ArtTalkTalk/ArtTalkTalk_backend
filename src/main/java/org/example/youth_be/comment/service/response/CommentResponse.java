package org.example.youth_be.comment.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.comment.repository.dto.ArtworkCommentQueryDto;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class CommentResponse {
    @Schema(description = "프로필 이미지 url")
    private String profileUrl;
    @Schema(description = "닉네임")
    private String nickname;
    @Schema(description = "작성일자")
    private LocalDateTime createdAt;
    @Schema(description = "댓글 내용")
    private String contents;

    public static CommentResponse of (ArtworkCommentQueryDto queryDto) {
        return new CommentResponse(
                queryDto.getProfileImage(),
                queryDto.getNickname(),
                queryDto.getCreatedAt(),
                queryDto.getContents()
        );
    }
}
