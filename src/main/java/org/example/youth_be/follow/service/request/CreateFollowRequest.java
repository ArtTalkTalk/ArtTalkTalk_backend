package org.example.youth_be.follow.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateFollowRequest {
    @Schema(description = "팔로우할 유저의 ID")
    private Long receiverId;
}
