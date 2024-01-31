package org.example.youth_be.artwork.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.controller.spec.ArtworkSpec;
import org.example.youth_be.artwork.service.ArtworkService;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.artwork.service.request.DevArtworkCreateRequest;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.PageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artworks")
public class ArtworkController implements ArtworkSpec {

    private final ArtworkService artworkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createArtworkForDev(@RequestBody DevArtworkCreateRequest request) {
        return artworkService.createArtworkForDev(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ArtworkResponse> getArtworks(@RequestParam Long userId, @RequestParam String type, @ModelAttribute ArtworkPaginationRequest request) {
        return artworkService.getArtworks(userId, type, request);
    }
}
