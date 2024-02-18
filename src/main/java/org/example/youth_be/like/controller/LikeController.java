package org.example.youth_be.like.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.like.controller.spec.LikeSpec;
import org.example.youth_be.like.service.LikeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artworks")
public class LikeController implements LikeSpec {
    private final LikeService likeService;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{artworkId}/likes")
    public void createArtworkLike(@PathVariable Long artworkId, @CurrentUser TokenClaim tokenClaim) {
        likeService.createArtworkLike(artworkId, tokenClaim);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{artworkId}/likes/{likeId}")
    public void deleteArtworkLike(@PathVariable Long artworkId, @PathVariable Long likeId, @CurrentUser TokenClaim tokenClaim) {
        likeService.deleteArtworkLike(artworkId, likeId, tokenClaim);
    }


}
