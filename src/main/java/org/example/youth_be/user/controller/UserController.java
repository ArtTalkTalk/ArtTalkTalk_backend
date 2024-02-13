package org.example.youth_be.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.user.controller.spec.UserSpec;
import org.example.youth_be.user.service.UserService;
import org.example.youth_be.user.service.request.*;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.user.service.response.UserMyInformation;
import org.example.youth_be.user.service.response.UserMyPage;
import org.example.youth_be.user.service.response.UserProfileResponse;
import org.example.youth_be.user.service.response.UserSignUpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserSpec {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long signup(@Valid @RequestBody UserSignupRequest request) {
        return userService.signup(request);
    }

    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public void checkNicknameDuplicate(@RequestParam String nickname) {
        userService.checkNicknameDuplicate(nickname);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileResponse getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId);
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateUserProfile(@PathVariable Long userId, @RequestBody UserProfileUpdateRequest request) {
        userService.updateUserProfile(userId, request);
    }

    @PostMapping("/{userId}/links")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUserLink(@PathVariable Long userId, @RequestBody LinkRequest request) {
        return userService.createUserLink(userId, request);
    }

    @DeleteMapping("/{userId}/links/{linkId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserLink(@PathVariable Long userId, @PathVariable Long linkId) {
        userService.deleteUserLink(userId, linkId);
    }

    @Override
    @PutMapping("/{userId}/links/{linkId}")
    @ResponseStatus(HttpStatus.OK)
    public Long updateUserLink(@PathVariable Long userId, @PathVariable Long linkId, @RequestBody UserLinkUpdateRequest request) {
        return userService.updateUserLink(userId, linkId, request);
    }

    @GetMapping("/{userId}/artworks")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ArtworkResponse> getUserArtworks(@PathVariable Long userId, @RequestParam ArtworkMyPageType type, @ModelAttribute ArtworkPaginationRequest request) {
        return userService.getUserArtworks(userId, type, request);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserMyInformation getMyInformation(@CurrentUser TokenClaim tokenClaim) {
        return userService.getMyInformation(tokenClaim);
    }

    @PutMapping("/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public UserSignUpResponse signUp(@CurrentUser TokenClaim tokenClaim, @RequestBody UserAdditionSignupRequest request) {
        return userService.signUp(tokenClaim, request);
    }

    @GetMapping("/mypage")
    @ResponseStatus(HttpStatus.OK)
    public UserMyPage getMyPage(@CurrentUser TokenClaim tokenClaim) {
        return userService.getMyPage(tokenClaim);
    }
}
