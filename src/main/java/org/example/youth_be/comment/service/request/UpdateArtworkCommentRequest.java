package org.example.youth_be.comment.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArtworkCommentRequest {
    @Schema(description = "댓글 내용")
    private String contents;
}
