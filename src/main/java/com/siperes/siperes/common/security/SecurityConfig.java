package com.siperes.siperes.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.siperes.siperes.common.util.Constants.CommonPats.SECURE_LIST_PATS;
import static com.siperes.siperes.common.util.Constants.ManageIngredient.MANAGE_INGREDIENT_PATS_ALL;
import static com.siperes.siperes.common.util.Constants.ManageRecipe.MANAGE_RECIPE_PATS_ALL;
import static com.siperes.siperes.enumeration.EnumPermission.*;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(GET, MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(USER_READ.getPermission())
                        .requestMatchers(POST, MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(USER_CREATE.getPermission())
                        .requestMatchers(PUT, MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(USER_UPDATE.getPermission())
                        .requestMatchers(DELETE, MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(USER_DELETE.getPermission())
                        .requestMatchers(GET, MANAGE_INGREDIENT_PATS_ALL).hasAnyAuthority(ADMIN_READ.getPermission())
                        .requestMatchers(POST, MANAGE_INGREDIENT_PATS_ALL).hasAnyAuthority(ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, MANAGE_INGREDIENT_PATS_ALL).hasAnyAuthority(ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, MANAGE_INGREDIENT_PATS_ALL).hasAnyAuthority(ADMIN_DELETE.getPermission())
                        .requestMatchers(SECURE_LIST_PATS)
                        .authenticated()
                        .anyRequest()
                        .permitAll()
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint)
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(Customizer.withDefaults());
        return http.build();
    }
}
