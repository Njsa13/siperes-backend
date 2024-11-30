package com.spiceswap.spiceswap.common.security;

import com.spiceswap.spiceswap.common.util.Constants;
import com.spiceswap.spiceswap.enumeration.EnumPermission;
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
                        .requestMatchers(GET, Constants.ManageRecipe.MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(EnumPermission.USER_READ.getPermission())
                        .requestMatchers(POST, Constants.ManageRecipe.MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(EnumPermission.USER_CREATE.getPermission())
                        .requestMatchers(PUT, Constants.ManageRecipe.MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(EnumPermission.USER_UPDATE.getPermission())
                        .requestMatchers(DELETE, Constants.ManageRecipe.MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(EnumPermission.USER_DELETE.getPermission())
                        .requestMatchers(GET, Constants.ModificationRequest.MODIFICATION_REQUEST_PATS_ALL).hasAnyAuthority(EnumPermission.USER_READ.getPermission())
                        .requestMatchers(POST, Constants.ModificationRequest.MODIFICATION_REQUEST_PATS_ALL).hasAnyAuthority(EnumPermission.USER_CREATE.getPermission())
                        .requestMatchers(PUT, Constants.ModificationRequest.MODIFICATION_REQUEST_PATS_ALL).hasAnyAuthority(EnumPermission.USER_UPDATE.getPermission())
                        .requestMatchers(DELETE, Constants.ModificationRequest.MODIFICATION_REQUEST_PATS_ALL).hasAnyAuthority(EnumPermission.USER_DELETE.getPermission())
                        .requestMatchers(GET, Constants.RecipeReview.RECIPE_REVIEW_PATS_ALL).hasAnyAuthority(EnumPermission.USER_READ.getPermission())
                        .requestMatchers(POST, Constants.RecipeReview.RECIPE_REVIEW_PATS_ALL).hasAnyAuthority(EnumPermission.USER_CREATE.getPermission())
                        .requestMatchers(PUT, Constants.RecipeReview.RECIPE_REVIEW_PATS_ALL).hasAnyAuthority(EnumPermission.USER_UPDATE.getPermission())
                        .requestMatchers(DELETE, Constants.RecipeReview.RECIPE_REVIEW_PATS_ALL).hasAnyAuthority(EnumPermission.USER_DELETE.getPermission())
                        .requestMatchers(GET, Constants.ManageIngredient.MANAGE_INGREDIENT_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_READ.getPermission())
                        .requestMatchers(POST, Constants.ManageIngredient.MANAGE_INGREDIENT_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, Constants.ManageIngredient.MANAGE_INGREDIENT_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, Constants.ManageIngredient.MANAGE_INGREDIENT_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_DELETE.getPermission())
                        .requestMatchers(GET, Constants.AdminManageRecipe.ADMIN_MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_READ.getPermission())
                        .requestMatchers(POST, Constants.AdminManageRecipe.ADMIN_MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, Constants.AdminManageRecipe.ADMIN_MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, Constants.AdminManageRecipe.ADMIN_MANAGE_RECIPE_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_DELETE.getPermission())
                        .requestMatchers(GET, Constants.AdminManageUser.ADMIN_MANAGE_USER_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_READ.getPermission())
                        .requestMatchers(POST, Constants.AdminManageUser.ADMIN_MANAGE_USER_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_CREATE.getPermission())
                        .requestMatchers(PUT, Constants.AdminManageUser.ADMIN_MANAGE_USER_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_UPDATE.getPermission())
                        .requestMatchers(DELETE, Constants.AdminManageUser.ADMIN_MANAGE_USER_PATS_ALL).hasAnyAuthority(EnumPermission.ADMIN_DELETE.getPermission())
                        .requestMatchers(Constants.CommonPats.SECURE_LIST_PATS)
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
