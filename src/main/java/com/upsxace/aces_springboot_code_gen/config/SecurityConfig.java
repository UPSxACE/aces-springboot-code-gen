package com.upsxace.aces_springboot_code_gen.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Value("${SPRING_PROFILES_ACTIVE:dev}")
    private String activeProfile;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(c -> {
                if(!activeProfile.equals("prod")){
                    c.requestMatchers("/swagger-ui/**").permitAll()
                     .requestMatchers("/swagger-ui.html").permitAll()
                     .requestMatchers("/v3/api-docs/**").permitAll();
                }

                c.requestMatchers(HttpMethod.POST, "/sql-to-code").permitAll()
                 .anyRequest().authenticated();
            })
            .exceptionHandling(c -> {
                c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                c.accessDeniedHandler(((request, response, accessDeniedException) ->
                        response.setStatus(HttpStatus.FORBIDDEN.value()))
                );
            });

        return http.build();
    }
}
