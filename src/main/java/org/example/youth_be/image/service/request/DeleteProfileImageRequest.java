package org.example.youth_be.image.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteProfileImageRequest {


    private Long userId;
    private String imageUrl;
}
