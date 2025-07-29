package com.authguard.authguard.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.authguard.authguard.services.ClientService;
import com.authguard.authguard.services.JwtService;
import com.authguard.authguard.services.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final ClientService clientService;
    private final UserService userService;
    private final PasswordEncoder pswdEncoder;
    private final JwtService jwtService;

    @Bean
    @Order(1)
    SecurityFilterChain clientAuthFilterChain(HttpSecurity httpSecurity) throws Exception {
        // System.out.println("inside filter client");

        httpSecurity.cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .securityMatcher("/auth/client/**", "/client/**", "/apps/**")
                .authorizeHttpRequests(
                        (auth) -> auth.requestMatchers("/auth/client/**", "app/info/**").permitAll().anyRequest()
                                .authenticated())
                .csrf(crsfConfig -> crsfConfig.disable())
                .addFilterBefore(new ClientJwtAuthFilter(jwtService, clientService),
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain userAuthFitlerChain(HttpSecurity httpSecurity) throws Exception {
        // System.out.println("inside filter user");

        httpSecurity.securityMatcher("/user/**")
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(
                        (auth) -> auth.requestMatchers("/auth/user/**").permitAll().anyRequest().authenticated())
                .csrf(crsfConfig -> crsfConfig.disable())
                .addFilterBefore(new UserJwtAuthFilter(jwtService, userService),
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    @Order(3)
    SecurityFilterChain oauth2FilterChain(HttpSecurity httpSecurity) throws Exception {
        System.out.println("inside filter oauth2");
        httpSecurity
                .securityMatcher("/oauth2/**")
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/oauth2/**",
                                "/login/**",
                                "/userinfo",
                                "/.well-known/**")
                        .permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable());
        return httpSecurity.build();
    }

    @Bean
    DaoAuthenticationProvider clientAuthProvider() {
        DaoAuthenticationProvider daoprovider = new DaoAuthenticationProvider(clientService);
        daoprovider.setPasswordEncoder(pswdEncoder);
        return daoprovider;
    }

    @Bean
    DaoAuthenticationProvider userAuthProvider() {
        DaoAuthenticationProvider daoprovider = new DaoAuthenticationProvider(userService);
        daoprovider.setPasswordEncoder(pswdEncoder);
        return daoprovider;
    }

    @Bean("userAuthManager")
    AuthenticationManager userAuthenticationManager() {
        List<AuthenticationProvider> providers = List.of(userAuthProvider());
        return new ProviderManager(providers);
    }

    @Bean("clientAuthManager")
    @Primary
    AuthenticationManager clientAuthenticationManager() {
        List<AuthenticationProvider> providers = List.of(clientAuthProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:8081"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
