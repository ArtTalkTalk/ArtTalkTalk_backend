package org.example.youth_be.comment.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateArtworkCommentRequest {
    @Schema(description = "댓글 내용, 글자 제한 없음")
    @NotNull
    private String contents;
}
