package org.example.youth_be.common.security.dto;

import lombok.*;
import org.example.youth_be.user.enums.UserRoleEnum;

@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@Builder
public class SecurityUserDto {
    private Long userId;
    private UserRoleEnum role;
}
