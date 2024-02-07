package org.example.youth_be.image.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.image.service.request.DeleteArtworkImageRequest;
import org.example.youth_be.image.service.request.DeleteProfileImageRequest;
import org.example.youth_be.image.service.request.UploadProfileImageRequest;
import org.example.youth_be.image.service.response.UploadImageResponse;

@Tag(name = ApiTags.IMAGE)
public interface ImageSpec {
    @Operation(description = "프로필 이미지 업로드 API입니다.")
    UploadImageResponse uploadProfileImage(UploadProfileImageRequest request);

    void deleteProfileImage(DeleteProfileImageRequest request);

    void deleteArtworkImage(DeleteArtworkImageRequest request);
}
