package org.example.youth_be.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "jwt")
@AllArgsConstructor
public class JwtProperties {
    private String secretKey;
    private Long accessValidityInDays;
    private Long refreshValidityInDays;
}
