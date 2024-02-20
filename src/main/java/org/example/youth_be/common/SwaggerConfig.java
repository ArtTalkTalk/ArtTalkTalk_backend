package org.example.youth_be.common;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {@Server(url = "https://www.art-talktalk.store", description = "서버 도메인")},
        info = @Info(title = "청춘예찬 App",
                description = "청춘예찬 api명세",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("청춘예찬 API v1")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public OpenAPI api() {
        String accessKey = "Access Token";
        String refreshKey = "Refresh Token";

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(accessKey)
                .addList(refreshKey);


        SecurityScheme accessToken = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityScheme refreshToken = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Authorization-refresh");

        Components components = new Components()
                .addSecuritySchemes(accessKey, accessToken)
                .addSecuritySchemes(refreshKey, refreshToken);

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}