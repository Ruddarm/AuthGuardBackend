package com.authguard.authguard.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
        httpSecurity.cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .securityMatcher("/auth/client/**", "/client/**")
                .authorizeHttpRequests(
                        (auth) -> auth.requestMatchers("/auth/client/**").permitAll().anyRequest().authenticated())
                .csrf(crsfConfig -> crsfConfig.disable())
                .addFilterBefore(new ClientJwtAuthFilter(jwtService, clientService),
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain userAuthFitlerChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher("/auth/user/**", "/user/**")
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(
                        (auth) -> auth.requestMatchers("/auth/user/**").permitAll().anyRequest().authenticated())
                .csrf(crsfConfig -> crsfConfig.disable())
                .addFilterBefore(new UserJwtAuthFilter(jwtService,userService), UsernamePasswordAuthenticationFilter.class);
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

    @Bean
    AuthenticationManager authenticationManager() {
        List<AuthenticationProvider> providers = List.of(clientAuthProvider(), userAuthProvider());
        return new ProviderManager(providers);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
