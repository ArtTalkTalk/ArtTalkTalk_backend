package org.example.youth_be.user.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateLinkResponse {
    private Long userId;
    private Long linkId;
}
