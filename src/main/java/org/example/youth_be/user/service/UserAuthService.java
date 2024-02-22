package org.example.youth_be.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.exceptions.YouthForbiddenException;
import org.example.youth_be.common.exceptions.YouthNotFoundException;
import org.example.youth_be.common.jwt.ParsedTokenInfo;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.common.jwt.TokenProvider;
import org.example.youth_be.common.redis.TokenRepository;
import org.example.youth_be.user.domain.UserEntity;
import org.example.youth_be.user.enums.UserRoleEnum;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.request.DevTokenGenerateRequest;
import org.example.youth_be.user.service.request.LoginRequest;
import org.example.youth_be.user.service.request.SignupRequest;
import org.example.youth_be.user.service.request.TokenReissueRequest;
import org.example.youth_be.user.service.response.GenerateTokensForDev;
import org.example.youth_be.user.service.response.LoginResponse;
import org.example.youth_be.user.service.response.SignUpResponse;
import org.example.youth_be.user.service.response.TokenReissueResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthService {
    private final UserRepository userRepository;
    private final TokenProvider accessTokenProvider;
    private final TokenProvider refreshTokenProvider;
    private final TokenRepository tokenRepository;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Optional<UserEntity> userEntityOptional = userRepository.findBySocialIdAndSocialType(request.getSocialId(), request.getSocialType());
        UserEntity userEntity = userEntityOptional.orElseGet(() -> userRepository.save(UserEntity.builder()
                .socialId(request.getSocialId())
                .socialType(request.getSocialType())
                .userRole(UserRoleEnum.ASSOCIATE)
                .build()));
        String accessToken = accessTokenProvider.generateToken(userEntity.getUserId(), userEntity.getUserRole());
        String refreshToken = refreshTokenProvider.generateToken(userEntity.getUserId(), userEntity.getUserRole());
        return new LoginResponse(accessToken, refreshToken, userEntity.getUserRole());
    }

    @Transactional
    public SignUpResponse signUp(TokenClaim tokenClaim, String accessToken, String refreshToken, SignupRequest request) {
        Long userId = tokenClaim.getUserId();

        // 정회원 토큰 생성
        String newAccessToken = accessTokenProvider.generateToken(userId, UserRoleEnum.REGULAR);
        String newRefreshToken = refreshTokenProvider.generateToken(userId, UserRoleEnum.REGULAR);


        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));
        userEntity.signUp(request.getProfileImageUrl(), request.getNickname(),
                request.getActivityField(), request.getActivityArea(), request.getDescription());

        // 기존 토큰 무효화
        tokenRepository.setBlackList(accessToken, String.valueOf(userId));
        tokenRepository.setBlackList(refreshToken, String.valueOf(userId));

        // user role 업데이트 및 request 기반 업데이트/ 중복 닉네임 확인
        return SignUpResponse.builder().userId(userEntity.getUserId()).role(userEntity.getUserRole()).accessToken(newAccessToken).refreshToken(newRefreshToken).build();
    }

    @Transactional(readOnly = true)
    public TokenReissueResponse reissue(TokenReissueRequest request) {

        if(tokenRepository.isInBlackList(request.getRefreshToken())){
            throw new YouthBadRequestException("리프레시 토큰이 올바르지 않습니다.", null);
        }

        ParsedTokenInfo accessTokenInfo = accessTokenProvider.parseToken(request.getAccessToken());
        ParsedTokenInfo refreshTokenInfo = refreshTokenProvider.parseToken(request.getRefreshToken());

        validateAccessToken(accessTokenInfo);
        validateRefreshToken(refreshTokenInfo);
        if (!accessTokenInfo.isSameUserId(refreshTokenInfo)) {
            throw new YouthBadRequestException("리프레시, 액세스 토큰이 일치하지 않습니다.", null);
        }

        UserEntity userEntity = userRepository.findById(accessTokenInfo.getTokenClaim().getUserId())
                .orElseThrow(() -> new YouthNotFoundException("유저를 찾을 수 없습니다.", null));
        String generatedAccessToken = accessTokenProvider.generateToken(userEntity.getUserId(), userEntity.getUserRole());
        return new TokenReissueResponse(generatedAccessToken);
    }

    @Transactional(readOnly = true)
    public GenerateTokensForDev generateForDev(DevTokenGenerateRequest request) {
        String generatedAccessToken = accessTokenProvider.generateTokenForDev(request.getUserId(), request.getUserRole(), request.getAccessValidityInSeconds());
        String generatedRefreshToken = refreshTokenProvider.generateTokenForDev(request.getUserId(), request.getUserRole(), request.getRefreshValidityInSeconds());
        return new GenerateTokensForDev(generatedAccessToken, generatedRefreshToken);
    }

    @Transactional(readOnly = true)
    public void logout(TokenClaim tokenClaim, String accessToken, String refreshToken) {

        Long userId = tokenClaim.getUserId();
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new YouthNotFoundException("해당 ID의 유저를 찾을 수 없습니다.", null));


        tokenRepository.setBlackList(accessToken, String.valueOf(userEntity.getUserId()));
        tokenRepository.setBlackList(refreshToken, String.valueOf(userEntity.getUserId()));
    }

    private void validateRefreshToken(ParsedTokenInfo refreshTokenInfo) {
        if (refreshTokenInfo.isExpired()) {
            throw new YouthForbiddenException("리프레시 토큰이 만료되었습니다.", "리프레시 토큰이 만료되었습니다.");
        }
        if (!refreshTokenInfo.isNormalToken()) {
            throw new YouthBadRequestException("올바르지 않은 리프레시 토큰입니다.", null);
        }
    }

    private void validateAccessToken(ParsedTokenInfo accessTokenInfo) {
        if (!accessTokenInfo.isExpired()) {
            throw new YouthBadRequestException("만료되지 않았거나 올바르지 않은 액세스 토큰입니다.", null);
        }
    }
}
