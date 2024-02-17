package org.example.youth_be.comment.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.comment.service.request.CreateArtworkCommentRequest;
import org.example.youth_be.comment.service.response.CommentResponse;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.common.PageParam;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.jwt.TokenClaim;

@Tag(name = ApiTags.USER)
public interface CommentSpec {
    @Operation(description = "작품 댓글 조회 API")
    PageResponse<CommentResponse> getAllArtworkComments(Long artworkId, PageParam pageParam);
    @Operation(description = "작품 댓글 작성 API")
    Long createArtworkComment(TokenClaim claim, Long artworkId, CreateArtworkCommentRequest request);
    @Operation(description = "작품 댓글 삭제 API")
    void deleteArtworkComment(TokenClaim claim, Long artworkId, Long commentId);
}
