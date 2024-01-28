package org.example.youth_be.user.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private Optional<String> profileImageUrl;
    private String nickname;
    private Optional<String> major;
    private Optional<String> description;
    private Optional<String> link;
}
