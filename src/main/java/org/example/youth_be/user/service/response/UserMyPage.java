package org.example.youth_be.user.service.response;

import lombok.Builder;
import lombok.Getter;
import org.example.youth_be.artwork.service.response.ArtworkResponse;
import org.example.youth_be.common.PageResponse;

@Getter
@Builder
public class UserMyPage {

    private UserProfileResponse userProfileResponse;
    private PageResponse<ArtworkResponse> artworkResponsePageResponse;

}
