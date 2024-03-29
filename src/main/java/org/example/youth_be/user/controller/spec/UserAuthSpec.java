package org.example.youth_be.user.controller.spec;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.youth_be.common.ApiTags;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.user.service.request.DevTokenGenerateRequest;
import org.example.youth_be.user.service.request.LoginRequest;
import org.example.youth_be.user.service.request.SignupRequest;
import org.example.youth_be.user.service.request.TokenReissueRequest;
import org.example.youth_be.user.service.response.GenerateTokensForDev;
import org.example.youth_be.user.service.response.LoginResponse;
import org.example.youth_be.user.service.response.SignUpResponse;
import org.example.youth_be.user.service.response.TokenReissueResponse;

@Tag(name = ApiTags.USER_AUTH)
public interface UserAuthSpec {
    @Operation(description = "로그인 API")
    LoginResponse login(LoginRequest request) throws InterruptedException;

    @Operation(description = "추가 회원가입 API")
    SignUpResponse signUp(TokenClaim tokenClaim, String accessToken, String refreshToken, SignupRequest request);

    @Operation(description = "토큰 재발급 API")
    TokenReissueResponse reissue(TokenReissueRequest request);
    @Operation(description = "개발용 토큰 재발급 API\n\n만료시간은 초 단위입니다.")
    GenerateTokensForDev generatedTokensForDev(DevTokenGenerateRequest request);

    @Operation(description = "로그아웃 API")
    void logout(TokenClaim tokenClaim, String accessToken, String refreshToken);
}
