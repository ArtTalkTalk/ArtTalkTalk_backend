package org.example.youth_be.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.enums.ArtworkOtherPageType;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.user.controller.spec.UserSpec;
import org.example.youth_be.user.service.UserService;
import org.example.youth_be.user.service.request.*;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.user.service.response.CreateLinkResponse;
import org.example.youth_be.user.service.response.UserMyInformation;
import org.example.youth_be.user.service.response.UserMyPage;
import org.example.youth_be.user.service.response.UserProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserSpec {
    private final UserService userService;

    @Override
    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public void checkNicknameDuplicate(@RequestParam String nickname) {
        userService.checkNicknameDuplicate(nickname);
    }

    @Override
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse getUserProfile(@PathVariable Long userId, @CurrentUser TokenClaim claim) {
        return userService.getUserProfile(userId, claim);
    }

    @Override
    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserProfile(@PathVariable Long userId, @RequestBody UserProfileUpdateRequest request) {
        userService.updateUserProfile(userId, request);
    }

    @Override
    @PostMapping("/{userId}/links")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateLinkResponse createUserLink(@PathVariable Long userId, @RequestBody LinkRequest request, @CurrentUser TokenClaim claim) {
        return userService.createUserLink(userId, request, claim);
    }

    @Override
    @DeleteMapping("/{userId}/links/{linkId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserLink(@PathVariable Long userId, @PathVariable Long linkId, @CurrentUser TokenClaim claim) {
        userService.deleteUserLink(userId, linkId, claim);
    }

    @Override
    @PutMapping("/{userId}/links/{linkId}")
    @ResponseStatus(HttpStatus.OK)
    public Long updateUserLink(@PathVariable Long userId, @PathVariable Long linkId, @RequestBody UserLinkUpdateRequest request, @CurrentUser TokenClaim claim) {
        return userService.updateUserLink(userId, linkId, request, claim);
    }

    @Override
    @GetMapping("/artworks")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ArtworkResponse> getUserArtworks(@CurrentUser TokenClaim tokenClaim, @RequestParam ArtworkMyPageType type, @ModelAttribute ArtworkPaginationRequest request) {
        return userService.getUserArtworks(tokenClaim, type, request);
    }

    @Override
    @GetMapping("/{userId}/artworks")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ArtworkResponse> getOtherUserArtworks(@PathVariable Long userId, @RequestParam ArtworkOtherPageType type, @ModelAttribute ArtworkPaginationRequest request) {
        return userService.getOtherUserArtworks(userId, type, request);
    }

    @Override
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserMyInformation getMyInformation(@CurrentUser TokenClaim tokenClaim) {
        return userService.getMyInformation(tokenClaim);
    }

    @Override
    @GetMapping("/mypage")
    @ResponseStatus(HttpStatus.OK)
    public UserMyPage getMyPage(@CurrentUser TokenClaim tokenClaim) {
        return userService.getMyPage(tokenClaim);
    }
}
