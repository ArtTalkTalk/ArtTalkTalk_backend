package org.example.youth_be.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.image.controller.spec.ImageSpec;
import org.example.youth_be.image.service.ImageService;
import org.example.youth_be.image.service.request.DeleteImageRequest;
import org.example.youth_be.image.service.request.ImageUploadRequest;
import org.example.youth_be.image.service.response.UploadImageResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController implements ImageSpec {

    private final ImageService imageService;

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadImageResponse uploadImage(@ModelAttribute ImageUploadRequest request){
        return imageService.uploadImage(request);
    }

    @DeleteMapping("/profile")
    public void deleteImage(@RequestBody DeleteImageRequest request){
        imageService.deleteImage(request);
    }
}
