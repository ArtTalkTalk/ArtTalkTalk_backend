package org.example.youth_be.image.service.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ImageUploadRequest {
    private MultipartFile file;
}
