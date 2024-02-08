package org.example.youth_be.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.exceptions.YouthAccessTokenExpireException;
import org.example.youth_be.common.jwt.dto.ParsedTokenInfo;
import org.example.youth_be.common.security.dto.SecurityUserDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private final TokenProvider tokenProvider;

	// 토큰 검사 생략
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().contains("/"); // 재발급 앤드포인트
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// request Header에서 AccessToken을 가져온다.
		String accessToken = request.getHeader(AUTHORIZATION_HEADER);

		// 토큰 검사 생략(모두 허용 URL의 경우 토큰 검사 통과)
		if (!StringUtils.hasText(accessToken)) {
			doFilter(request, response, filterChain);
			return;
		}

		ParsedTokenInfo result = tokenProvider.parseToken(accessToken);

		if (result.getType() != null) {
			if (result.getType().equals("EXPIRED_EXCEPTION")) {
				// 만료되었을경우 예외를 발생시킨다.
				throw new YouthAccessTokenExpireException("유효하지 않은 토큰입니다.", null);
			} else if (result.getType().equals("MALFORM_EXCEPTION")) {
				// 형식이 올바르지 않은 경우
				throw new YouthAccessTokenExpireException("형식에 맞지 않은 토큰입니다.", null); // 추후 변경
				}
			}

		// SecurityContext에 등록할 User 객체를 만들어준다.
		SecurityUserDto userDto = SecurityUserDto.builder()
				.userId(result.getUserInfo().getUserId())
				.role(result.getUserInfo().getRole())
				.build();

		// SecurityContext에 인증 객체를 등록해준다.
		Authentication auth = getAuthentication(userDto);
		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

	public Authentication getAuthentication(SecurityUserDto user) {
		return new UsernamePasswordAuthenticationToken(user, "",
				List.of(new SimpleGrantedAuthority(user.getRole().getDescription())));
	}
}


