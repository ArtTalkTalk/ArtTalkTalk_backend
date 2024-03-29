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
import org.example.youth_be.common.redis.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String AUTHORIZATION_HEADER = HttpHeaders.AUTHORIZATION;
	private static final List<String> WHITE_LIST = List.of("/h2-console/", "/v1/health-check", "/swagger-ui/", "/login", "/reissue", "/dev-tokens");
	private TokenProvider tokenProvider;
	private TokenRepository tokenRepository;

	@Autowired
	public JwtAuthenticationFilter(@MainAccessTokenProvider TokenProvider tokenProvider, TokenRepository tokenRepository) {
		this.tokenProvider = tokenProvider;
		this.tokenRepository = tokenRepository;
	}

	// 토큰 검사 생략
	public boolean hasWhiteList(HttpServletRequest request) {
		return WHITE_LIST.stream().anyMatch(url -> url.equals(request.getRequestURI()));
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info("JwtAuthenticationFilter.doFilterInternal");
		log.info("request.getRequestURI() : {}", request.getRequestURI());

		// request Header에서 AccessToken을 가져온다.
		String accessToken = request.getHeader(AUTHORIZATION_HEADER);
		ParsedTokenInfo tokenInfo = tokenProvider.parseToken(accessToken);
		try {
			// 토큰 검사 생략(모두 허용 URL의 경우 토큰 검사 통과)
			if (hasWhiteList(request)) {
				log.info("검사 통과");
				doFilter(request, response, filterChain);
				return;
			}

			// 블랙리스트 검사
			if(tokenRepository.hasBlackList(accessToken)){
				throw new YouthBadRequestException("사용할 수 없는 토큰입니다.", null);
			}

			validateAccessToken(tokenInfo);

			// SecurityContext에 등록할 객체
			TokenClaim tokenClaim = tokenInfo.getTokenClaim();

			// SecurityContext에 인증 객체를 등록해준다.
			Authentication auth = getAuthentication(tokenClaim);
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception ex) {
			request.setAttribute("exception", ex);
		}

		filterChain.doFilter(request, response);
	}

	private void validateAccessToken(ParsedTokenInfo tokenInfo) {
		if (tokenInfo.isNormalToken()) {
			return;
		}
		JwtThrowableType throwableType = tokenInfo.getThrowableType();
		log.info("throwableType : {}", tokenInfo.getThrowableType());
		switch (throwableType) {
			case EXPIRED:
				throw new YouthUnAuthorizationException(throwableType.getDescription(), null);

			case SIGNATURE_INVALID:
			case UNSUPPORTED:
			case MALFORMED:
			case NULL_OR_EMPTY:
				throw new YouthBadRequestException(throwableType.getDescription(), null);

			case UNHANDLED_EXCEPTION:
				throw new YouthInternalException(throwableType.getDescription(), null);

			default:
				break;
		}
	}

	public Authentication getAuthentication(TokenClaim tokenClaim) {
		return new UsernamePasswordAuthenticationToken(tokenClaim, "",
				List.of(new SimpleGrantedAuthority("ROLE_"+ String.valueOf(tokenClaim.getUserRole()))));
	}
}


