package org.example.youth_be.common.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.youth_be.common.jwt.dto.UserInfo;
import org.example.youth_be.common.jwt.enums.JwtThrowableEnum;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ParsedTokenInfo {
    private UserInfo userInfo;
    private JwtThrowableEnum type;
}
