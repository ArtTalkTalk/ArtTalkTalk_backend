package org.example.youth_be.common.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.user.enums.UserRoleEnum;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private Long userId;
    private UserRoleEnum role;
}
