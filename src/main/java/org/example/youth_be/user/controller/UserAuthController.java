package org.example.youth_be.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.CurrentUser;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.user.controller.spec.UserAuthSpec;
import org.example.youth_be.user.service.UserAuthService;
import org.example.youth_be.user.service.request.DevTokenGenerateRequest;
import org.example.youth_be.user.service.request.LoginRequest;
import org.example.youth_be.user.service.request.SignupRequest;
import org.example.youth_be.user.service.request.TokenReissueRequest;
import org.example.youth_be.user.service.response.GenerateTokensForDev;
import org.example.youth_be.user.service.response.LoginResponse;
import org.example.youth_be.user.service.response.SignUpResponse;
import org.example.youth_be.user.service.response.TokenReissueResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserAuthController implements UserAuthSpec {
    private final UserAuthService userAuthService;

    @Override
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return userAuthService.login(request);
    }

    @Override
    @PutMapping("/sign-up")
    @ResponseStatus(HttpStatus.OK)
    public SignUpResponse signUp(@CurrentUser TokenClaim tokenClaim,
                                 @RequestHeader("Authorization") String accessToken,
                                 @RequestHeader("Authorization-refresh") String refreshToken,
                                 @RequestBody @Valid SignupRequest request) {
        return userAuthService.signUp(tokenClaim, accessToken, refreshToken, request);
    }

    @Override
    @PostMapping("/reissue")
    public TokenReissueResponse reissue(@RequestBody @Valid TokenReissueRequest request) {
        return userAuthService.reissue(request);
    }



    @Override
    @PostMapping("/dev-tokens")
    public GenerateTokensForDev generatedTokensForDev(@RequestBody DevTokenGenerateRequest request) {
        return userAuthService.generateForDev(request);
    }

    @Override
    @DeleteMapping("/logout")
    public void logout(@CurrentUser TokenClaim tokenClaim,
                       @RequestHeader("Authorization") String accessToken,
                       @RequestHeader("Authorization-refresh") String refreshToken) {
        userAuthService.logout(tokenClaim, accessToken, refreshToken);
    }
}
