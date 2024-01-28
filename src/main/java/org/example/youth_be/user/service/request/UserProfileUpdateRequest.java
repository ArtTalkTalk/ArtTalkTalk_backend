package org.example.youth_be.user.service.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class UserProfileUpdateRequest {
    private String profileImageUrl;
    private String nickname;
    private String major;
    private String description;
    private String link;
}
