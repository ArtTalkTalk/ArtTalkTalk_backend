package org.example.youth_be.follow.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.follow.controller.spec.FollowSpec;
import org.example.youth_be.follow.service.FollowService;
import org.example.youth_be.follow.service.request.CreateFollowRequest;
import org.example.youth_be.follow.service.response.CreateFollowResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class FollowController implements FollowSpec {
    private final FollowService followService;

    @Override
    @PostMapping("/users/{userId}/follows")
    public CreateFollowResponse createFollow(
            @CurrentUser TokenClaim claim,
            @PathVariable Long userId,
            @RequestBody CreateFollowRequest request
    ) {
        return followService.createFollow(claim, userId, request.getReceiverId());
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
