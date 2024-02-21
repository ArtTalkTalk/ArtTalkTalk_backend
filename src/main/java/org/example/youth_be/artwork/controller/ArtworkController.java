package org.example.youth_be.artwork.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.controller.spec.ArtworkSpec;
import org.example.youth_be.artwork.service.ArtworkService;
import org.example.youth_be.artwork.service.request.ArtworkCreateRequest;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.request.ArtworkUpdateRequest;
import org.example.youth_be.artwork.service.response.ArtworkDetailResponse;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.jwt.TokenClaim;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artworks")
public class ArtworkController implements ArtworkSpec {

    private final ArtworkService artworkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createArtwork(@CurrentUser TokenClaim tokenClaim, @RequestBody @Valid ArtworkCreateRequest request) {
        return artworkService.createArtwork(tokenClaim, request);
    }

    // 전체 artwork 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ArtworkResponse> getArtworks(@ModelAttribute ArtworkPaginationRequest request) {
        return artworkService.getArtworks(request);
    }

    // 팔로잉한 유저들의 artwork 조회
    @GetMapping("/following")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ArtworkResponse> getArtworksFollowing(@CurrentUser TokenClaim tokenClaim, @ModelAttribute ArtworkPaginationRequest request) {
        return artworkService.getArtworksFollowing(tokenClaim, request);

    }

    @GetMapping("/{artworkId}")
    @ResponseStatus(HttpStatus.OK)
    public ArtworkDetailResponse getArtwork(@PathVariable Long artworkId, @CurrentUser TokenClaim claim) {
        return artworkService.getArtwork(claim, artworkId);

    }

    @PutMapping("/{artworkId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateArtwork(@CurrentUser TokenClaim tokenClaim, @PathVariable Long artworkId, @RequestBody @Valid ArtworkUpdateRequest request) {
        artworkService.updateArtwork(tokenClaim, artworkId, request);
    }

    @DeleteMapping("/{artworkId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteArtwork(@CurrentUser TokenClaim tokenClaim, @PathVariable Long artworkId) {
        artworkService.deleteArtwork(tokenClaim, artworkId);
    }
}
