package org.example.youth_be.common.security;

import lombok.RequiredArgsConstructor;
import org.example.youth_be.common.jwt.JwtAccessDeniedHandler;
import org.example.youth_be.common.jwt.JwtAuthenticationFilter;
import org.example.youth_be.user.enums.UserRoleEnum;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final String[] PATTERNS = {
            "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**"
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
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(AbstractHttpConfigurer::disable) //폼 로그인 비활성
                .httpBasic(AbstractHttpConfigurer::disable) //HTTP 기본인증 비활성
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(Stream.of(PATTERNS).map(AntPathRequestMatcher::antMatcher).toArray(AntPathRequestMatcher[]::new)).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/artworks/**")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/users/{userId}")).permitAll()
                        .requestMatchers(new MvcRequestMatcher(introspector, "/users/{userId}/artworks")).permitAll() // 회원이 아니어도 접근 가능
                        .requestMatchers(new MvcRequestMatcher(introspector, "/추가회원가입")).hasRole(String.valueOf(UserRoleEnum.ASSOCIATE)) // 준회원은 추가 회원가입 가능
                        .anyRequest().hasRole(String.valueOf(UserRoleEnum.REGULAR)) // 정회원은 모든 api 접근 가능
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(jwtAccessDeniedHandler) // 406
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

/*
.authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers(Stream.of(PATTERNS).map(AntPathRequestMatcher::antMatcher).toArray(AntPathRequestMatcher[]::new)).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/artworks/**"),
                                new AntPathRequestMatcher("/users/{userId}"),
                                new AntPathRequestMatcher("/users/{userId}/artworks")).permitAll() // 회원이 아니어도 접근 가능
                        .requestMatchers(new AntPathRequestMatcher("/추가회원가입")).hasRole(String.valueOf(UserRoleEnum.ASSOCIATE)) // 준회원은 추가 회원가입 가능
                        .anyRequest().hasRole(String.valueOf(UserRoleEnum.REGULAR)) // 정회원은 모든 api 접근 가능
                )
 */

