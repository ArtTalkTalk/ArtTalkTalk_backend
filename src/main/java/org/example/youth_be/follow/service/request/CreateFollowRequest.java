package org.example.youth_be.follow.service.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFollowRequest {
    @Schema(description = "팔로우할 유저의 ID")
    @NotNull
    private Long receiverId;
}
