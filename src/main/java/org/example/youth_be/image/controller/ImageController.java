package org.example.youth_be.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.image.controller.spec.ImageSpec;
import org.example.youth_be.image.service.ImageService;
import org.example.youth_be.image.service.request.ImageUploadRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController implements ImageSpec {

    private final ImageService imageService;

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@ModelAttribute ImageUploadRequest request) throws Exception {
        return ResponseEntity.ok(imageService.uploadImage(request));
    }
}
