package org.example.youth_be.artwork.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.controller.spec.ArtworkSpec;
import org.example.youth_be.artwork.service.ArtworkService;
import org.example.youth_be.artwork.service.request.DevArtworkCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/artworks")
public class ArtworkController implements ArtworkSpec {

    private final ArtworkService artworkService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createArtworkForDev(@RequestBody DevArtworkCreateRequest request) {
        artworkService.createArtworkForDev(request);
    }
}
