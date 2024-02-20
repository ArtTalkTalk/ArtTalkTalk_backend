package org.example.youth_be.comment.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.comment.controller.spec.CommentSpec;
import org.example.youth_be.comment.service.CommentService;
import org.example.youth_be.comment.service.request.CreateArtworkCommentRequest;
import org.example.youth_be.comment.service.response.CommentResponse;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.PageParam;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.jwt.TokenClaim;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artworks")
public class CommentController implements CommentSpec {
    private final CommentService commentService;

    @Override
    @GetMapping("/{artworkId}/comments")
    public PageResponse<CommentResponse> getAllArtworkComments(@CurrentUser TokenClaim claim, @PathVariable Long artworkId, @ModelAttribute PageParam pageParam) {
        return commentService.getArtworkComments(claim, artworkId, pageParam);
    }
    @Override
    @PostMapping("/{artworkId}/comments")
    public void createArtworkComment(@CurrentUser TokenClaim claim, @PathVariable Long artworkId, CreateArtworkCommentRequest request) {
        commentService.createArtworkComment(claim, artworkId, request);
    }

    @Override
    @DeleteMapping("/{artworkId}/comments/{commentId}")
    public void deleteArtworkComment(@CurrentUser TokenClaim claim, @PathVariable Long artworkId, @PathVariable Long commentId) {
        commentService.deleteArtworkComment(claim, artworkId, commentId);
    }
}
