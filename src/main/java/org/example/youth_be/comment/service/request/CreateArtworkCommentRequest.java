package org.example.youth_be.comment.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateArtworkCommentRequest {
    @Schema(description = "댓글 내용, 글자 제한 없음")
    private String contents;
}
