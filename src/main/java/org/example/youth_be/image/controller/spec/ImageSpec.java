package org.example.youth_be.image.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.image.service.request.ImageUploadRequest;
import org.springframework.http.ResponseEntity;

@Tag(name = ApiTags.IMAGE)
public interface ImageSpec {
    @Operation(description = "프로필 이미지 업로드 API입니다.")
    ResponseEntity<?> uploadImage(ImageUploadRequest request);
}
