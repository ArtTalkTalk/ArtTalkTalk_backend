package org.example.youth_be.user.service.request;

import lombok.Getter;
import org.example.youth_be.user.enums.SocialTypeEnum;
import org.example.youth_be.user.enums.UserRoleEnum;

import java.util.List;

@Getter
public class DevUserProfileCreateRequest {
    private String profileImageUrl;
    private SocialTypeEnum socialType;
    private UserRoleEnum userRole;
    private String socialId;
    private String nickname;
    private String activityField;
    private String activityArea;
    private String description;
    private List<LinkRequest> linkRequestList;
    private Long totalLikeCount;
    private Long followerCount;
}
