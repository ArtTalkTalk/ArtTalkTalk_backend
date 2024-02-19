package org.example.youth_be.follow.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.follow.controller.spec.FollowSpec;
import org.example.youth_be.follow.service.FollowService;
import org.example.youth_be.follow.service.response.CreateFollowResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController implements FollowSpec {
    private final FollowService followService;

    @Override
    @PostMapping("/users/{userId}/follows")
    public CreateFollowResponse createFollow(
            @CurrentUser TokenClaim claim,
            @PathVariable Long userId
    ) {
        return followService.createFollow(claim.getUserId(), userId);
    }

    @Override
    @DeleteMapping("/users/{userId}/follows/{followId}")
    public void deleteFollow(
            @CurrentUser TokenClaim claim,
            @PathVariable Long userId,
            @PathVariable Long followId
    ) {
        followService.deleteFollow(claim.getUserId(), userId, followId);
    }
}
