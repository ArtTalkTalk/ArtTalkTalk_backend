package org.example.youth_be.user.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.youth_be.user.domain.UserLinkEntity;

@Getter
@AllArgsConstructor
public class LinkResponse {
    private Long linkId;
    private String title;
    private String address;

    static public LinkResponse of(UserLinkEntity userLinkEntity) {
        return new LinkResponse(
                userLinkEntity.getId(),
                userLinkEntity.getTitle(),
                userLinkEntity.getLinkUrl());
    }
}
