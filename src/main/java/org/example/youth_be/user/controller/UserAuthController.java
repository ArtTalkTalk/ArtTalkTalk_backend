package org.example.youth_be.user.controller;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.user.controller.spec.UserAuthSpec;
import org.example.youth_be.user.service.UserAuthService;
import org.example.youth_be.user.service.request.DevTokenGenerateRequest;
import org.example.youth_be.user.service.request.LoginRequest;
import org.example.youth_be.user.service.request.TokenReissueRequest;
import org.example.youth_be.user.service.response.LoginResponse;
import org.example.youth_be.user.service.response.TokenReissueResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAuthController implements UserAuthSpec {
    private final UserAuthService userAuthService;

    @Override
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userAuthService.login(request);
    }

    @Override
    @PostMapping("/reissue")
    public TokenReissueResponse reissue(@RequestBody TokenReissueRequest request) {
        return userAuthService.reissue(request);
    }

    @Override
    @PostMapping("/dev-tokens")
    public TokenReissueResponse generatedTokensForDev(@RequestBody DevTokenGenerateRequest request) {
        return userAuthService.generateForDev(request);
    }
}
