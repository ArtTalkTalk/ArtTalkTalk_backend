package org.example.youth_be.user.service;

import org.example.youth_be.artwork.repository.ArtworkRepository;
import org.example.youth_be.common.jwt.TokenClaim;
import org.example.youth_be.user.enums.UserRoleEnum;
import org.example.youth_be.user.repository.UserLinkRepository;
import org.example.youth_be.user.repository.UserRepository;
import org.example.youth_be.user.service.response.UserMyInformation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;
    @Mock
    UserLinkRepository userLinkRepository;
    @Mock
    ArtworkRepository artworkRepository;


    @Test
    @DisplayName("토큰 정보를 그대로 반환해주는 테스트")
    void getMyInformation() {

        //given
        TokenClaim tokenClaim = new TokenClaim(1L, "sub", 1L, 10000L, UserRoleEnum.ASSOCIATE);

        // When
        UserMyInformation result = userService.getMyInformation(tokenClaim);

        // Then
        assertNotNull(result, "결과는 null이 아니어야 합니다.");
        assertEquals(tokenClaim.getUserId(), result.getUserId(), "userId는 tokenClaim의 userId와 일치해야 합니다.");
        assertEquals(tokenClaim.getUserRole(), result.getRole(), "역할은 tokenClaim의 userRole과 일치해야 합니다.");    }
}