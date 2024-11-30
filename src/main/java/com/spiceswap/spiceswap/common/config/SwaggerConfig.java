package com.spiceswap.spiceswap.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SwaggerConfig {

        @Value("${swagger.server.url}")
        private String serverUrl;

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .addServersItem(new Server().url(serverUrl))
                        .info(new Info().title("SpiceSwap")
                                .version("v1.0.0")
                                .description("Swagger UI for SpiceSwap"))
                        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                        .components(new Components().addSecuritySchemes("bearerAuth",
                                new SecurityScheme().name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .description("JWT auth description")));
        }
}
