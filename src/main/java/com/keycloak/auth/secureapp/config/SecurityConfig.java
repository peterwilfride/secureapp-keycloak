package com.keycloak.auth.secureapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                //.requestMatchers("/public/**")
                .requestMatchers("/admin/users").authenticated()
                .requestMatchers("/admin/users/**").permitAll()
                //.permitAll()
                .anyRequest()
                .permitAll()
                .and()
                .oauth2ResourceServer().jwt();
        return http.build();
    }
}
