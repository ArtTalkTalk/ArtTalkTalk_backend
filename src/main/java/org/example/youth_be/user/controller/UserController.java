package org.example.youth_be.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.artwork.enums.ArtworkMyPageType;
import org.example.youth_be.artwork.service.request.ArtworkPaginationRequest;
import org.example.youth_be.common.PageResponse;
import org.example.youth_be.user.controller.spec.UserSpec;
import org.example.youth_be.user.service.UserService;
import org.example.youth_be.user.service.request.UserCreateRequest;
import org.example.youth_be.user.service.request.LinkRequest;
import org.example.youth_be.user.service.request.UserProfileUpdateRequest;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.user.service.response.UserProfileResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserSpec {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
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

    @GetMapping("/{userId}/artworks")
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<ArtworkResponse> getUserArtworks(@PathVariable Long userId, @RequestParam ArtworkMyPageType type, @ModelAttribute ArtworkPaginationRequest request) {
        return userService.getUserArtworks(userId, type, request);
    }
}
