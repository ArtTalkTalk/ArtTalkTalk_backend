package org.example.youth_be.follow.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateFollowResponse {
    @Schema(description = "팔로우 ID")
    private Long followId;
}
