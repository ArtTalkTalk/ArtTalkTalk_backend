package org.example.youth_be.user.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLinkUpdateRequest {
    @Schema(description = "링크 제목")
    private String title;
    @Schema(description = "링크 URL")
    private String url;
}
