package org.example.youth_be.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.user.controller.spec.UserSpec;
import org.example.youth_be.user.service.UserService;
import org.example.youth_be.user.service.dto.DevUserProfileCreateRequest;
import org.example.youth_be.user.service.dto.UserProfileDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserSpec {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUserForDev(@RequestBody DevUserProfileCreateRequest request) {
        userService.createUserForDev(request);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserProfileDto getUserProfile(@PathVariable Long userId) {
        return userService.getUserProfile(userId);
    }
}
