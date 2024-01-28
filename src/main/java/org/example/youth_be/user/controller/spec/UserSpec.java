package org.example.youth_be.user.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.user.service.request.DevUserProfileCreateRequest;
import org.example.youth_be.user.service.response.UserProfileDto;

@Tag(name = ApiTags.USER)
public interface UserSpec {
    @Operation(description = "개발용 유저 생성 API [값을 넣지 않으면 서버에서 임의로 넣습니다.]")
    void createUserForDev(DevUserProfileCreateRequest request);

    @Operation(description = "유저 프로필 조회 API")
    UserProfileDto getUserProfile(Long userId);
}
