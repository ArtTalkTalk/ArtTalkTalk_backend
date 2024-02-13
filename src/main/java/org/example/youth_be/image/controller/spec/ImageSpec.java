package org.example.youth_be.image.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.image.service.request.DeleteImageRequest;
import org.example.youth_be.image.service.request.ImageUploadRequest;
import org.example.youth_be.image.service.response.UploadArtworkImageResponse;
import org.example.youth_be.image.service.response.UploadImageResponse;

@Tag(name = ApiTags.IMAGE)
public interface ImageSpec {
    @Operation(description = "프로필 이미지 업로드 API입니다.")
    UploadImageResponse uploadImage(ImageUploadRequest request);

    void deleteImage (DeleteImageRequest request);

    UploadArtworkImageResponse uploadArtworkImage(ImageUploadRequest request);
}
