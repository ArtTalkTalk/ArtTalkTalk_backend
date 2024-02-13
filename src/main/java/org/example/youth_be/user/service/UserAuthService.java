package org.example.youth_be.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.TokenProvider;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.LoginRequest;
import org.example.youth_be.user.service.response.LoginResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthService {
    private final UserRepository userRepository;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        UserEntity userEntity = userRepository.findBySocialIdAndSocialType(request.getSocialId(), request.getSocialType())
                .orElseThrow(() -> new YouthNotFoundException("가입되지 않은 회원입니다. 회원가입을 해주세요", null));
        String accessToken = accessTokenProvider.generateToken(userEntity.getUserId(), userEntity.getUserRole());
        String refreshToken = refreshTokenProvider.generateToken(userEntity.getUserId(), userEntity.getUserRole());
        return new LoginResponse(accessToken, refreshToken, userEntity.getUserRole());
    }
}
