package org.example.youth_be.common.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.youth_be.common.exceptions.YouthBadRequestException;
import org.example.youth_be.common.exceptions.YouthInternalException;
import org.example.youth_be.common.exceptions.YouthUnAuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
	private static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
	private TokenProvider tokenProvider;

	@Autowired
	public JwtAuthenticationFilter(@MainAccessTokenProvider TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	// 토큰 검사 생략
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getRequestURI().contains("/test"); // 재발급 앤드포인트
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info("JwtAuthenticationFilter.doFilterInternal");

		// request Header에서 AccessToken을 가져온다.
		String accessToken = request.getHeader(AUTHORIZATION_HEADER);

		// 토큰 검사 생략(모두 허용 URL의 경우 토큰 검사 통과)
		if (!StringUtils.hasText(accessToken) && shouldNotFilter(request)) {
			doFilter(request, response, filterChain);
			return;
		}

		ParsedTokenInfo result = tokenProvider.parseToken(accessToken);
		log.info("result.getThrowableType() : {}", result.getThrowableType());
		try {
			switch (result.getThrowableType()) {
				case EXPIRED:
					throw new YouthUnAuthorizationException(result.getThrowableType().getDescription(), null);

				case SIGNATURE_INVALID:
				case UNSUPPORTED:
				case MALFORMED:
				case NULL_OR_EMPTY:
					throw new YouthBadRequestException(result.getThrowableType().getDescription(), null);

				case UNHANDLED_EXCEPTION:
					throw new YouthInternalException(result.getThrowableType().getDescription(), null);

				default:
					break;
			}

			// SecurityContext에 등록할 객체
			TokenClaim tokenClaim = result.getTokenClaim();

			// SecurityContext에 인증 객체를 등록해준다.
			Authentication auth = getAuthentication(tokenClaim);
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception ex) {
			request.setAttribute("exception", ex);
		}

		filterChain.doFilter(request, response);
	}

	public Authentication getAuthentication(TokenClaim tokenClaim) {
		return new UsernamePasswordAuthenticationToken(tokenClaim, "",
				List.of(new SimpleGrantedAuthority(tokenClaim.getUserRole().toString())));
	}
}


