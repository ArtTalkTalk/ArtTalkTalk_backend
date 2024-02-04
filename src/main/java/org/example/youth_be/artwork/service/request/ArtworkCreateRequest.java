package org.example.youth_be.artwork.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.artwork.enums.ArtworkStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class ArtworkCreateRequest {
    @Schema(description = "작품 이미지 url 리스트")
    private MultipartFile[] images;
    @Schema(description = "작품 제목")
    private String title;
    @Schema(description = "작품 설명")
    private String description;
    @Schema(description = "작품 상태", example = "PUBLIC, SELLING, FREE")
    private ArtworkStatus artworkStatus;
    @Schema(description = "작품 포스팅 유저 아이디(추후에 소셜 로그인 구현 시 삭제)")
    private Long userId;

}
