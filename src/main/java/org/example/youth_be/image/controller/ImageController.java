package org.example.youth_be.image.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.image.controller.spec.ImageSpec;
import org.example.youth_be.image.service.ImageService;
import org.example.youth_be.image.service.request.DeleteArtworkImageRequest;
import org.example.youth_be.image.service.request.DeleteProfileImageRequest;
import org.example.youth_be.image.service.request.UploadProfileImageRequest;
import org.example.youth_be.image.service.response.UploadImageResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController implements ImageSpec {

    private final ImageService imageService;

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadImageResponse uploadProfileImage(@ModelAttribute UploadProfileImageRequest request){
        return imageService.uploadProfileImage(request);
    }

    @DeleteMapping("/profile")
    public void deleteProfileImage(@RequestBody DeleteProfileImageRequest request){
        imageService.deleteProfileImage(request);
    }

    @DeleteMapping("/artwork")
    public void deleteArtworkImage(@RequestBody DeleteArtworkImageRequest request){
        imageService.deleteArtworkImage(request);
    }
}
