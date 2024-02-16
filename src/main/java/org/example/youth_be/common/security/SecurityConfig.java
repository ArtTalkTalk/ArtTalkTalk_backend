package org.example.youth_be.common.security;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.jwt.JwtAccessDeniedHandler;
import org.example.youth_be.common.jwt.JwtAuthenticationFilter;
import org.example.youth_be.user.enums.UserRoleEnum;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.stream.Stream;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationEntryPoint entryPoint;
    private static final String[] PATTERNS = {
            "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**", "/v1/health-check", "/swagger-ui/**",
            "/swagger-resources/**", "/v3/api-docs/**"
    };


    /*
     * CORS 설정
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Authorization-refresh");
        config.addExposedHeader("Set-Cookie");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    protected SecurityFilterChain filterChain(final HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .headers(HeadersConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable) //폼 로그인 비활성
                .httpBasic(AbstractHttpConfigurer::disable) //HTTP 기본인증 비활성
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(Stream.of(PATTERNS).map(AntPathRequestMatcher::antMatcher).toArray(AntPathRequestMatcher[]::new)).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/users/me")).hasAnyRole(UserRoleEnum.REGULAR.name(), UserRoleEnum.ASSOCIATE.name())
                        .requestMatchers(
                                antMatcher(HttpMethod.POST, "/image/profile"),
                                antMatcher(HttpMethod.DELETE, "/image/profile"))
                                .hasAnyRole(UserRoleEnum.REGULAR.name(), UserRoleEnum.ASSOCIATE.name()
                        )
                        .requestMatchers(antMatcher(HttpMethod.GET, "/artworks")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/users/{userId}")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.GET, "/users/{userId}/artworks")).permitAll() // 회원이 아니어도 접근 가능
                        .requestMatchers(antMatcher(HttpMethod.GET, "/error")).permitAll()
                        .requestMatchers(antMatcher(HttpMethod.PUT, "/sign-up")).hasRole(String.valueOf(UserRoleEnum.ASSOCIATE)) // 준회원은 추가 회원가입 가능
                        .requestMatchers(antMatcher(HttpMethod.GET, "/users/check")).hasRole(String.valueOf(UserRoleEnum.ASSOCIATE))
                        .requestMatchers(antMatcher(HttpMethod.GET, "/users/mypage")).hasRole(String.valueOf(UserRoleEnum.REGULAR))
                        .requestMatchers(antMatcher(HttpMethod.GET, "/users/check")).hasRole(String.valueOf(UserRoleEnum.REGULAR))
                        .requestMatchers(antMatcher(HttpMethod.GET, "/users/artworks")).hasRole(String.valueOf(UserRoleEnum.REGULAR))
                        .requestMatchers(antMatcher(HttpMethod.GET, "/artworks/following")).hasRole(String.valueOf(UserRoleEnum.REGULAR))
                        .requestMatchers(antMatcher("/**")).hasRole(String.valueOf(UserRoleEnum.REGULAR))
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(jwtAccessDeniedHandler) // 406
                                .authenticationEntryPoint(entryPoint)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}